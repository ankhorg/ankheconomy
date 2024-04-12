package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;

public interface BindPlayerEconomyApi {

  String render(String currency, BigDecimal amount);

  BigDecimal get(String currency);

  void set(String currency, BigDecimal amount);

  void add(String currency, BigDecimal amount);

  boolean subtract(String currency, BigDecimal amount);

  AnkhEconomyApi unbindPlayer();

  BindAllEconomyApi bindCurrency(String currency);

  AnkhEconomyApi unbind();
}
