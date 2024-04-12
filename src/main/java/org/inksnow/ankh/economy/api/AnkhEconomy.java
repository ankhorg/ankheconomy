package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;

@SuppressWarnings("rawtypes") // override by platform
public final class AnkhEconomy {

  private static AnkhEconomyApi api;

  private AnkhEconomy() {
    throw new UnsupportedOperationException();
  }

  public static String render(OfflinePlayer player, String currency, BigDecimal amount) {
    return instance().render(player, currency, amount);
  }

  public static BigDecimal get(OfflinePlayer player, String currency) {
    return instance().get(player, currency);
  }

  public static void set(OfflinePlayer player, String currency, BigDecimal amount) {
    instance().set(player, currency, amount);
  }

  public static void add(OfflinePlayer player, String currency, BigDecimal amount) {
    instance().add(player, currency, amount);
  }

  public static boolean subtract(OfflinePlayer player, String currency, BigDecimal amount) {
    return instance().subtract(player, currency, amount);
  }

  public static BindPlayerEconomyApi bindPlayer(OfflinePlayer player) {
    return instance().bindPlayer(player);
  }

  public static BindCurrencyEconomyApi bindCurrency(String currency) {
    return instance().bindCurrency(currency);
  }

  public static BindAllEconomyApi bindAll(OfflinePlayer player, String currency) {
    return instance().bindAll(player, currency);
  }

  public static AnkhEconomyApi instance() {
    if (api == null) {
      throw new IllegalStateException("AnkhEconomyApi instance is not set");
    }
    return api;
  }

  @SuppressWarnings("checkstyle:TypeName") // internal use class
  public static final class $internal$actions {

    @SuppressWarnings("checkstyle:MethodName") // internal use method
    public static synchronized void setInstance(AnkhEconomyApi api) {
      if (AnkhEconomy.api == null) {
        AnkhEconomy.api = api;
      } else {
        throw new IllegalArgumentException("AnkhEconomyApi instance is already set");
      }
    }
  }
}
