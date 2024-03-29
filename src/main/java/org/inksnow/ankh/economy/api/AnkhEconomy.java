package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;

@SuppressWarnings("rawtypes") // override by platform
public final class AnkhEconomy {

  private static AnkhEconomyApi api;

  private AnkhEconomy() {
    throw new UnsupportedOperationException();
  }

  public static String render(Object player, String currency, BigDecimal amount) {
    return instance().render(player, currency, amount);
  }

  public static BigDecimal get(Object player, String currency) {
    return instance().get(player, currency);
  }

  public static void set(Object player, String currency, BigDecimal amount) {
    instance().set(player, currency, amount);
  }

  public static void add(Object player, String currency, BigDecimal amount) {
    instance().add(player, currency, amount);
  }

  public static boolean subtract(Object player, String currency, BigDecimal amount) {
    return instance().subtract(player, currency, amount);
  }

  public static <P> BindPlayerEconomyApi<P> bindPlayer(P player) {
    return (BindPlayerEconomyApi<P>) instance().bindPlayer(player);
  }

  public static <P> BindCurrencyEconomyApi<P> bindCurrency(String currency) {
    return (BindCurrencyEconomyApi<P>) instance().bindCurrency(currency);
  }

  public static <P> BindAllEconomyApi<P> bindAll(P player, String currency) {
    return (BindAllEconomyApi<P>) instance().bindAll(player, currency);
  }

  @SuppressWarnings("checkstyle:MethodName") // internal use method
  public static synchronized void _setInstance(AnkhEconomyApi api) {
    if (AnkhEconomy.api == null) {
      AnkhEconomy.api = api;
    } else {
      throw new IllegalArgumentException("AnkhEconomyApi instance is already set");
    }
  }

  public static <P> AnkhEconomyApi<P> instance() {
    if (api == null) {
      throw new IllegalStateException("AnkhEconomyApi instance is not set");
    }
    return api;
  }
}
