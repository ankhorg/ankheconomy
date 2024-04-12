package org.inksnow.ankh.economy;

import java.math.BigDecimal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.inksnow.ankh.economy.api.AnkhEconomyApi;
import org.inksnow.ankh.economy.api.BindAllEconomyApi;
import org.inksnow.ankh.economy.api.BindPlayerEconomyApi;

@RequiredArgsConstructor
public class BindPlayerEconomyImpl implements BindPlayerEconomyApi {

  private final AnkhEconomyImpl ankhEconomy;
  private final OfflinePlayer player;

  @Override
  public @NonNull String render(@NonNull String currency, @NonNull BigDecimal amount) {
    return ankhEconomy.render(player, currency, amount);
  }

  @Override
  public @NonNull BigDecimal get(@NonNull String currency) {
    return ankhEconomy.get(player, currency);
  }

  @Override
  public void set(@NonNull String currency, @NonNull BigDecimal amount) {
    ankhEconomy.set(player, currency, amount);
  }

  @Override
  public void add(@NonNull String currency, @NonNull BigDecimal amount) {
    ankhEconomy.add(player, currency, amount);
  }

  @Override
  public boolean subtract(@NonNull String currency, @NonNull BigDecimal amount) {
    return ankhEconomy.subtract(player, currency, amount);
  }

  @Override
  public @NonNull AnkhEconomyApi unbindPlayer() {
    return ankhEconomy;
  }

  @Override
  public @NonNull BindAllEconomyApi bindCurrency(String currency) {
    return new BindAllEconomyImpl(ankhEconomy, player, currency);
  }

  @Override
  public @NonNull AnkhEconomyApi unbind() {
    return ankhEconomy;
  }
}
