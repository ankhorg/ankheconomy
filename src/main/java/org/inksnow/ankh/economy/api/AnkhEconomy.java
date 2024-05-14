package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class AnkhEconomy {

  private static @Nullable AnkhEconomyApi api;

  private AnkhEconomy() {
    throw new UnsupportedOperationException();
  }

  public static @NonNull String render(@NonNull OfflinePlayer player, @Nullable String currency,
      @NonNull BigDecimal amount) {
    return instance().render(player, currency, amount);
  }

  public static @NonNull BigDecimal get(@NonNull OfflinePlayer player,
      @Nullable String currency) {
    return instance().get(player, currency);
  }

  public static void set(@NonNull OfflinePlayer player, @Nullable String currency,
      @NonNull BigDecimal amount) {
    instance().set(player, currency, amount);
  }

  public static void add(@NonNull OfflinePlayer player, @Nullable String currency,
      @NonNull BigDecimal amount) {
    instance().add(player, currency, amount);
  }

  public static boolean subtract(@NonNull OfflinePlayer player, @Nullable String currency,
      @NonNull BigDecimal amount) {
    return instance().subtract(player, currency, amount);
  }

  public static @NonNull BindPlayerEconomyApi bindPlayer(@NonNull OfflinePlayer player) {
    return instance().bindPlayer(player);
  }

  public static @NonNull BindCurrencyEconomyApi bindCurrency(@Nullable String currency) {
    return instance().bindCurrency(currency);
  }

  public static @NonNull BindAllEconomyApi bindAll(@NonNull OfflinePlayer player,
      @Nullable String currency) {
    return instance().bindAll(player, currency);
  }

  public static @NonNull AnkhEconomyApi instance() {
    if (api == null) {
      throw new IllegalStateException("AnkhEconomyApi instance is not set");
    }
    return api;
  }

  @SuppressWarnings("checkstyle:TypeName") // internal use class
  public static final class $internal$actions {

    @SuppressWarnings("checkstyle:MethodName") // internal use method
    public static synchronized void setInstance(@NonNull AnkhEconomyApi api) {
      if (AnkhEconomy.api == null) {
        AnkhEconomy.api = api;
      } else {
        throw new IllegalArgumentException("AnkhEconomyApi instance is already set");
      }
    }
  }
}
