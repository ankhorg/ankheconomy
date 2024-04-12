package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;

public interface BindAllEconomyApi {

  String render(BigDecimal amount);

  BigDecimal get();

  void set(BigDecimal amount);

  void add(BigDecimal amount);

  boolean subtract(BigDecimal amount);

  BindCurrencyEconomyApi unbindPlayer();

  BindPlayerEconomyApi unbindCurrency();

  AnkhEconomyApi unbind();
}
