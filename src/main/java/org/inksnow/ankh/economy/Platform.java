package org.inksnow.ankh.economy;

import java.util.UUID;

public interface Platform<P> {

  String pluginConfigDirectory();

  UUID playerUuid(P player);

  P nameToPlayer(String playerName);

  P uuidToPlayer(UUID playerUuid);

  boolean checkPermission(P player, String permission);

  void sendMessage(P player, String message);
}
