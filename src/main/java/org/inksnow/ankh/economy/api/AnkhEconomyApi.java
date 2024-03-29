package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;

public interface AnkhEconomyApi<P> {

  String render(P player, String currency, BigDecimal amount);

  BigDecimal get(P player, String currency);

  void set(P player, String currency, BigDecimal amount);

  void add(P player, String currency, BigDecimal amount);

  boolean subtract(P player, String currency, BigDecimal amount);

  BindPlayerEconomyApi<P> bindPlayer(P player);

  BindCurrencyEconomyApi<P> bindCurrency(String currency);

  BindAllEconomyApi<P> bindAll(P player, String currency);
}
