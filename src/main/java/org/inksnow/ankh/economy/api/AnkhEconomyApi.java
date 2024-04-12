package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;

public interface AnkhEconomyApi {

  String render(OfflinePlayer player, String currency, BigDecimal amount);

  BigDecimal get(OfflinePlayer player, String currency);

  void set(OfflinePlayer player, String currency, BigDecimal amount);

  void add(OfflinePlayer player, String currency, BigDecimal amount);

  boolean subtract(OfflinePlayer player, String currency, BigDecimal amount);

  BindPlayerEconomyApi bindPlayer(OfflinePlayer player);

  BindCurrencyEconomyApi bindCurrency(String currency);

  BindAllEconomyApi bindAll(OfflinePlayer player, String currency);
}
