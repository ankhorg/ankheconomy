package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface BindAllEconomyApi {

  @NonNull String render(@NonNull BigDecimal amount);

  @NonNull BigDecimal get();

  void set(@NonNull BigDecimal amount);

  void add(@NonNull BigDecimal amount);

  boolean subtract(@NonNull BigDecimal amount);

  @NonNull BindCurrencyEconomyApi unbindPlayer();

  @NonNull BindPlayerEconomyApi unbindCurrency();

  @NonNull AnkhEconomyApi unbind();
}
