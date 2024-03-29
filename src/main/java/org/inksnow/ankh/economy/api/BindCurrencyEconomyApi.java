package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;

public interface BindCurrencyEconomyApi<P> {

  String render(P player, BigDecimal amount);

  BigDecimal get(P player);

  void set(P player, BigDecimal amount);

  void add(P player, BigDecimal amount);

  boolean subtract(P player, BigDecimal amount);

  BindAllEconomyApi<P> bindPlayer(P player);

  AnkhEconomyApi<P> unbindCurrency();

  AnkhEconomyApi<P> unbind();
}
