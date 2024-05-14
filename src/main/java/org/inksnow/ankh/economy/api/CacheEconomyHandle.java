package org.inksnow.ankh.economy.api;

import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CacheEconomyHandle extends EconomyHandle {

  void onPlayerJoin(@NonNull OfflinePlayer player);

  void onPlayerQuit(@NonNull OfflinePlayer player);
}
