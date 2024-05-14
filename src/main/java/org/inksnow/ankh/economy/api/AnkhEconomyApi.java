package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface AnkhEconomyApi {

  @NonNull String render(@NonNull OfflinePlayer player, @Nullable String currency,
      @NonNull BigDecimal amount);

  @NonNull BigDecimal get(@NonNull OfflinePlayer player, @Nullable String currency);

  void set(@NonNull OfflinePlayer player, @Nullable String currency,
      @NonNull BigDecimal amount);

  void add(@NonNull OfflinePlayer player, @Nullable String currency, @NonNull BigDecimal amount);

  boolean subtract(@NonNull OfflinePlayer player, @Nullable String currency,
      @NonNull BigDecimal amount);

  @NonNull BindPlayerEconomyApi bindPlayer(@NonNull OfflinePlayer player);

  @NonNull BindCurrencyEconomyApi bindCurrency(@Nullable String currency);

  @NonNull BindAllEconomyApi bindAll(@NonNull OfflinePlayer player, @Nullable String currency);
}
