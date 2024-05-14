package org.inksnow.ankh.economy.api;

import java.io.IOException;
import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.inksnow.ankh.economy.config.CurrencyConfig;

public interface EconomyHandle {

  @NonNull CurrencyConfig config();

  void shutdown();

  boolean canReload(@NonNull CurrencyConfig economyConfig);

  void reload(@NonNull CurrencyConfig economyConfig);

  boolean hasAccount(@NonNull OfflinePlayer player);

  @NonNull BigDecimal get(@NonNull OfflinePlayer player);

  void set(@NonNull OfflinePlayer player, @NonNull BigDecimal amount);

  interface Factory {

    @NonNull String name();

    @NonNull EconomyHandle create(@NonNull CurrencyConfig economyConfig)
        throws IOException;
  }
}
