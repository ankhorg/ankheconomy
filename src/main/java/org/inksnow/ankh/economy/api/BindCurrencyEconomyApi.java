package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface BindCurrencyEconomyApi {

  @NonNull String render(@NonNull OfflinePlayer player, @NonNull BigDecimal amount);

  @NonNull BigDecimal get(@NonNull OfflinePlayer player);

  void set(@NonNull OfflinePlayer player, @NonNull BigDecimal amount);

  void add(@NonNull OfflinePlayer player, @NonNull BigDecimal amount);

  boolean subtract(@NonNull OfflinePlayer player, @NonNull BigDecimal amount);

  @NonNull BindAllEconomyApi bindPlayer(@NonNull OfflinePlayer player);

  @NonNull AnkhEconomyApi unbindCurrency();

  @NonNull AnkhEconomyApi unbind();
}
