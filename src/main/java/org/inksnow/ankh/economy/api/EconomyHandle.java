package org.inksnow.ankh.economy.api;

import java.io.IOException;
import java.math.BigDecimal;
import org.inksnow.ankh.economy.Platform;
import org.inksnow.ankh.economy.config.CurrencyConfig;

public interface EconomyHandle<P> {

  CurrencyConfig config();

  void shutdown();

  boolean canReload(CurrencyConfig economyConfig);

  void reload(CurrencyConfig economyConfig);

  boolean hasAccount(P player);

  BigDecimal get(P player);

  void set(P player, BigDecimal amount);

  interface Factory {

    String name();

    <P> EconomyHandle<P> create(Platform<P> platform, CurrencyConfig economyConfig)
        throws IOException;
  }
}
