package org.inksnow.ankh.economy.api;

import java.io.IOException;
import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;
import org.inksnow.ankh.economy.config.CurrencyConfig;

public interface EconomyHandle {

  CurrencyConfig config();

  void shutdown();

  boolean canReload(CurrencyConfig economyConfig);

  void reload(CurrencyConfig economyConfig);

  boolean hasAccount(OfflinePlayer player);

  BigDecimal get(OfflinePlayer player);

  void set(OfflinePlayer player, BigDecimal amount);

  interface Factory {

    String name();

    EconomyHandle create(CurrencyConfig economyConfig)
        throws IOException;
  }
}
