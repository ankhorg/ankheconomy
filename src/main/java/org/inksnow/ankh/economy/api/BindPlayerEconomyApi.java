package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface BindPlayerEconomyApi {

  @NonNull String render(@Nullable String currency, @NonNull BigDecimal amount);

  @NonNull BigDecimal get(@Nullable String currency);

  void set(@Nullable String currency, @NonNull BigDecimal amount);

  void add(@Nullable String currency, @NonNull BigDecimal amount);

  boolean subtract(@Nullable String currency, @NonNull BigDecimal amount);

  @NonNull AnkhEconomyApi unbindPlayer();

  @NonNull BindAllEconomyApi bindCurrency(@Nullable String currency);

  @NonNull AnkhEconomyApi unbind();
}
