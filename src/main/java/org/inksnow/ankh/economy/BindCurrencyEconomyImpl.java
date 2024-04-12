package org.inksnow.ankh.economy;

import java.math.BigDecimal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.inksnow.ankh.economy.api.AnkhEconomyApi;
import org.inksnow.ankh.economy.api.BindAllEconomyApi;
import org.inksnow.ankh.economy.api.BindCurrencyEconomyApi;

@RequiredArgsConstructor
public class BindCurrencyEconomyImpl implements BindCurrencyEconomyApi {

  private final AnkhEconomyImpl ankhEconomy;
  private final String currency;

  @Override
  public @NonNull String render(@NonNull OfflinePlayer player, @NonNull BigDecimal amount) {
    return ankhEconomy.render(player, currency, amount);
  }

  @Override
  public @NonNull BigDecimal get(@NonNull OfflinePlayer player) {
    return ankhEconomy.get(player, currency);
  }

  @Override
  public void set(@NonNull OfflinePlayer player, @NonNull BigDecimal amount) {
    ankhEconomy.set(player, currency, amount);
  }

  @Override
  public void add(@NonNull OfflinePlayer player, @NonNull BigDecimal amount) {
    ankhEconomy.add(player, currency, amount);
  }

  @Override
  public boolean subtract(@NonNull OfflinePlayer player, @NonNull BigDecimal amount) {
    return ankhEconomy.subtract(player, currency, amount);
  }

  @Override
  public @NonNull BindAllEconomyApi bindPlayer(@NonNull OfflinePlayer player) {
    return new BindAllEconomyImpl(ankhEconomy, player, currency);
  }

  @Override
  public @NonNull AnkhEconomyApi unbindCurrency() {
    return ankhEconomy;
  }

  @Override
  public @NonNull AnkhEconomyApi unbind() {
    return ankhEconomy;
  }
}
