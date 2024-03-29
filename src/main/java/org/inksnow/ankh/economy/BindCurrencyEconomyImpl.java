package org.inksnow.ankh.economy;

import java.math.BigDecimal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.inksnow.ankh.economy.api.AnkhEconomyApi;
import org.inksnow.ankh.economy.api.BindAllEconomyApi;
import org.inksnow.ankh.economy.api.BindCurrencyEconomyApi;

@RequiredArgsConstructor
public class BindCurrencyEconomyImpl<P> implements BindCurrencyEconomyApi<P> {

  private final AnkhEconomyImpl<P> ankhEconomy;
  private final String currency;

  @Override
  public @NonNull String render(@NonNull P player, @NonNull BigDecimal amount) {
    return ankhEconomy.render(player, currency, amount);
  }

  @Override
  public @NonNull BigDecimal get(@NonNull P player) {
    return ankhEconomy.get(player, currency);
  }

  @Override
  public void set(@NonNull P player, @NonNull BigDecimal amount) {
    ankhEconomy.set(player, currency, amount);
  }

  @Override
  public void add(@NonNull P player, @NonNull BigDecimal amount) {
    ankhEconomy.add(player, currency, amount);
  }

  @Override
  public boolean subtract(@NonNull P player, @NonNull BigDecimal amount) {
    return ankhEconomy.subtract(player, currency, amount);
  }

  @Override
  public @NonNull BindAllEconomyApi<P> bindPlayer(@NonNull P player) {
    return new BindAllEconomyImpl<>(ankhEconomy, player, currency);
  }

  @Override
  public @NonNull AnkhEconomyApi<P> unbindCurrency() {
    return ankhEconomy;
  }

  @Override
  public @NonNull AnkhEconomyApi<P> unbind() {
    return ankhEconomy;
  }
}
