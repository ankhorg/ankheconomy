package org.inksnow.ankh.economy.api;

public interface CacheEconomyHandle<P> extends EconomyHandle<P> {

  void onPlayerJoin(P player);

  void onPlayerQuit(P player);
}
