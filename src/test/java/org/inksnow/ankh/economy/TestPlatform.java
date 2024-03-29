package org.inksnow.ankh.economy;

import java.util.UUID;

public class TestPlatform implements Platform<TestPlayer> {
  @Override
  public String pluginConfigDirectory() {
    return "./run/test/config";
  }

  @Override
  public UUID playerUuid(TestPlayer player) {
    return player.getUuid();
  }

  @Override
  public TestPlayer nameToPlayer(String playerName) {
    return null;
  }

  @Override
  public boolean checkPermission(TestPlayer player, String permission) {
    return true;
  }

  @Override
  public void sendMessage(TestPlayer player, String message) {
    System.out.println("->" + player.getUuid() + ": " + message);
  }
}
