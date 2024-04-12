package org.inksnow.ankh.economy.bukkit.placeholder;

import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.inksnow.ankh.economy.api.AnkhEconomy;
import org.inksnow.ankh.economy.bukkit.AnkhEconomyPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Log
@RequiredArgsConstructor
public class AnkhEconomyExpansion extends PlaceholderExpansion {

  private final AnkhEconomyPlugin plugin;

  @Override
  public @NotNull String getIdentifier() {
    return plugin.getDescription().getName();
  }

  @Override
  public @NotNull String getAuthor() {
    return String.join(", ", plugin.getDescription().getAuthors());
  }

  @Override
  public @NotNull String getVersion() {
    return plugin.getDescription().getVersion();
  }

  @Override
  public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
    if (!params.startsWith("eco_")) {
      return null;
    }
    switch (params) {
      case "eco_balance:": {
        return balanceRequest(player, params.substring(12));
      }
      default: {
        logger.warning("Unknown placeholder request: " + params);
        return null;
      }
    }
  }

  private String balanceRequest(OfflinePlayer player, String params) {
    StringTokenizer tokenizer = new StringTokenizer(params, ",");
    String currencyName = tokenizer.nextToken();
    return AnkhEconomy.render(player, currencyName, AnkhEconomy.get(player, currencyName));
  }
}
