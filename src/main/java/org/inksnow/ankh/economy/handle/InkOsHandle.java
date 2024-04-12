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

public class InkOsHandle implements EconomyHandle, AtomicEconomyHandle {
  private final Map<Player, Object> playerLocks = new WeakHashMap<>();
  private final Function<Player, Object> createPlayerLockFunction = player -> new Object();

  private CurrencyConfig config;
  private String key;

  public InkOsHandle(CurrencyConfig config) {
    reload(config);
  }

  @Override
  public CurrencyConfig config() {
    return config;
  }

  @Override
  public void shutdown() {
    //
  }

  @Override
  public boolean canReload(CurrencyConfig economyConfig) {
    return config.getType().equals(economyConfig.getType());
  }

  @Override
  public void reload(CurrencyConfig config) {
    this.config = config;
    this.key = config.getProperties().get("key");
  }

  @Override
  public boolean hasAccount(OfflinePlayer offlinePlayer) {
    if (!offlinePlayer.isOnline()) {
      throw new IllegalArgumentException("Player must be online");
    }
    Player player = offlinePlayer.getPlayer();
    return InkOS.getBigDecimal(player, key) != null;
  }

  @Override
  public BigDecimal get(OfflinePlayer offlinePlayer) {
    if (!offlinePlayer.isOnline()) {
      throw new IllegalArgumentException("Player must be online");
    }
    Player player = offlinePlayer.getPlayer();
    return InkOS.getBigDecimal(player, key, BigDecimal.ZERO);
  }

  @Override
  public void set(OfflinePlayer offlinePlayer, BigDecimal amount) {
    if (!offlinePlayer.isOnline()) {
      throw new IllegalArgumentException("Player must be online");
    }
    Player player = offlinePlayer.getPlayer();
    InkOS.setBigDecimal(player, key, amount);
  }

  @Override
  public boolean compareAndSet(OfflinePlayer offlinePlayer, BigDecimal expect, BigDecimal update) {
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
    public String name() {
      return "inkos";
    }

    @Override
    @SneakyThrows
    public EconomyHandle create(CurrencyConfig economyConfig) {
      return new InkOsHandle(economyConfig);
    }
  }
}
