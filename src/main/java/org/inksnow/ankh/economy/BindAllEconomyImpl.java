package org.inksnow.ankh.economy;

import java.math.BigDecimal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.inksnow.ankh.economy.api.AnkhEconomyApi;
import org.inksnow.ankh.economy.api.BindAllEconomyApi;
import org.inksnow.ankh.economy.api.BindCurrencyEconomyApi;
import org.inksnow.ankh.economy.api.BindPlayerEconomyApi;

@RequiredArgsConstructor
public class BindAllEconomyImpl implements BindAllEconomyApi {

  private final AnkhEconomyImpl ankhEconomy;
  private final OfflinePlayer player;
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
  public @NonNull BindCurrencyEconomyApi unbindPlayer() {
    return new BindCurrencyEconomyImpl(ankhEconomy, currency);
  }

  @Override
  public @NonNull BindPlayerEconomyApi unbindCurrency() {
    return new BindPlayerEconomyImpl(ankhEconomy, player);
  }

  @Override
  public @NonNull AnkhEconomyApi unbind() {
    return ankhEconomy;
  }
}
