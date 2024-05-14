package org.inksnow.ankh.economy.handle;

import bot.inker.inkos.InkOS;
import java.math.BigDecimal;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.inksnow.ankh.economy.api.AtomicEconomyHandle;
import org.inksnow.ankh.economy.api.EconomyHandle;
import org.inksnow.ankh.economy.config.CurrencyConfig;
import org.jetbrains.annotations.NotNull;

public class InkOsHandle implements EconomyHandle, AtomicEconomyHandle {

  private final Map<Player, Object> playerLocks = new WeakHashMap<>();
  private final Function<Player, Object> createPlayerLockFunction = player -> new Object();

  private CurrencyConfig config;
  private String key;

  public InkOsHandle(CurrencyConfig config) {
    reload(config);
  }

  @Override
  public @NotNull CurrencyConfig config() {
    return config;
  }

  @Override
  public void shutdown() {
    //
  }

  @Override
  public boolean canReload(@NotNull CurrencyConfig economyConfig) {
    return config.getType().equals(economyConfig.getType());
  }

  @Override
  public void reload(@NotNull CurrencyConfig config) {
    this.config = config;
    this.key = config.getProperties().get("key");
  }

  @Override
  public boolean hasAccount(@NotNull OfflinePlayer offlinePlayer) {
    if (!offlinePlayer.isOnline()) {
      throw new IllegalArgumentException("Player must be online");
    }
    Player player = offlinePlayer.getPlayer();
    return InkOS.getBigDecimal(player, key) != null;
  }

  @Override
  public @NotNull BigDecimal get(@NotNull OfflinePlayer offlinePlayer) {
    if (!offlinePlayer.isOnline()) {
      throw new IllegalArgumentException("Player must be online");
    }
    Player player = offlinePlayer.getPlayer();
    return InkOS.getBigDecimal(player, key, BigDecimal.ZERO);
  }

  @Override
  public void set(@NotNull OfflinePlayer offlinePlayer, @NotNull BigDecimal amount) {
    if (!offlinePlayer.isOnline()) {
      throw new IllegalArgumentException("Player must be online");
    }
    Player player = offlinePlayer.getPlayer();
    InkOS.setBigDecimal(player, key, amount);
  }

  @Override
  public boolean compareAndSet(@NotNull OfflinePlayer offlinePlayer, @NotNull BigDecimal expect, @NotNull BigDecimal update) {
    if (!offlinePlayer.isOnline()) {
      throw new IllegalArgumentException("Player must be online");
    }
    Player player = offlinePlayer.getPlayer();
    synchronized (playerLocks.computeIfAbsent(player, createPlayerLockFunction)) {
      BigDecimal current = InkOS.getBigDecimal(player, key, BigDecimal.ZERO);
      if (current.compareTo(expect) == 0) {
        InkOS.setBigDecimal(player, key, update);
        return true;
      }
      return false;
    }
  }

  public static class Factory implements EconomyHandle.Factory {

    @Override
    public @NotNull String name() {
      return "inkos";
    }

    @Override
    @SneakyThrows
    public @NotNull EconomyHandle create(@NotNull CurrencyConfig economyConfig) {
      return new InkOsHandle(economyConfig);
    }
  }
}
