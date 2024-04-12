package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;

public interface BindCurrencyEconomyApi {

  String render(OfflinePlayer player, BigDecimal amount);

  BigDecimal get(OfflinePlayer player);

  void set(OfflinePlayer player, BigDecimal amount);

  void add(OfflinePlayer player, BigDecimal amount);

  boolean subtract(OfflinePlayer player, BigDecimal amount);

  BindAllEconomyApi bindPlayer(OfflinePlayer player);

  AnkhEconomyApi unbindCurrency();

  AnkhEconomyApi unbind();
}
