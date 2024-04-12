package org.inksnow.ankh.economy;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.inksnow.ankh.economy.bukkit.AnkhEconomyPlugin;
import org.junit.jupiter.api.BeforeAll;

public class TestPluginLoad {
  private static ServerMock server;
  private static AnkhEconomyPlugin plugin;

  @BeforeAll
  public static synchronized void ensureSetup() {
    if (server == null) {
      server = MockBukkit.mock();
      plugin = MockBukkit.load(AnkhEconomyPlugin.class);
    }
  }

  public static ServerMock server() {
    ensureSetup();
    return server;
  }

  public static AnkhEconomyPlugin plugin() {
    ensureSetup();
    return plugin;
  }
}
