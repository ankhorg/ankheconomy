package org.inksnow.ankh.economy.api;

import org.bukkit.OfflinePlayer;

public interface CacheEconomyHandle extends EconomyHandle {

  void onPlayerJoin(OfflinePlayer player);

  void onPlayerQuit(OfflinePlayer player);
}
