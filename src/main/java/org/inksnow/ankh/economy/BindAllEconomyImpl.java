package org.inksnow.ankh.economy;

import java.math.BigDecimal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.inksnow.ankh.economy.api.AnkhEconomyApi;
import org.inksnow.ankh.economy.api.BindAllEconomyApi;
import org.inksnow.ankh.economy.api.BindCurrencyEconomyApi;
import org.inksnow.ankh.economy.api.BindPlayerEconomyApi;

@RequiredArgsConstructor
public class BindAllEconomyImpl<P> implements BindAllEconomyApi<P> {

  private final AnkhEconomyImpl<P> ankhEconomy;
  private final P player;
  private final String currency;

  @Override
  public @NonNull String render(@NonNull BigDecimal amount) {
    return ankhEconomy.render(player, currency, amount);
  }

  @Override
  public @NonNull BigDecimal get() {
    return ankhEconomy.get(player, currency);
  }

  @Override
  public void set(@NonNull BigDecimal amount) {
    ankhEconomy.set(player, currency, amount);
  }

  @Override
  public void add(@NonNull BigDecimal amount) {
    ankhEconomy.add(player, currency, amount);
  }

  @Override
  public boolean subtract(@NonNull BigDecimal amount) {
    return ankhEconomy.subtract(player, currency, amount);
  }

  @Override
  public @NonNull BindCurrencyEconomyApi<P> unbindPlayer() {
    return new BindCurrencyEconomyImpl<>(ankhEconomy, currency);
  }

  @Override
  public @NonNull BindPlayerEconomyApi<P> unbindCurrency() {
    return new BindPlayerEconomyImpl<>(ankhEconomy, player);
  }

  @Override
  public @NonNull AnkhEconomyApi<P> unbind() {
    return ankhEconomy;
  }
}
