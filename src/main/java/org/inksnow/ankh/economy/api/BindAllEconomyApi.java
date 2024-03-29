package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;

public interface BindAllEconomyApi<P> {

  String render(BigDecimal amount);

  BigDecimal get();

  void set(BigDecimal amount);

  void add(BigDecimal amount);

  boolean subtract(BigDecimal amount);

  BindCurrencyEconomyApi<P> unbindPlayer();

  BindPlayerEconomyApi<P> unbindCurrency();

  AnkhEconomyApi<P> unbind();
}
