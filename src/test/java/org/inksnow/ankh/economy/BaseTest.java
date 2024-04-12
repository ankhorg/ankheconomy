package org.inksnow.ankh.economy;

import java.math.BigDecimal;
import org.bukkit.entity.Player;
import org.inksnow.ankh.economy.api.AnkhEconomy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BaseTest {
  @Test
  public void test() {
    TestPluginLoad.ensureSetup();

    Player testPlayerA = TestPluginLoad.server().addPlayer("testPlayerA");

    AnkhEconomy.set(testPlayerA, null, BigDecimal.valueOf(233));
    Assertions.assertEquals(BigDecimal.valueOf(233), AnkhEconomy.get(testPlayerA, null));

    AnkhEconomy.add(testPlayerA, null, BigDecimal.valueOf(100));
    Assertions.assertEquals(BigDecimal.valueOf(333), AnkhEconomy.get(testPlayerA, null));

    Assertions.assertFalse(AnkhEconomy.subtract(testPlayerA, null, BigDecimal.valueOf(334)));
    Assertions.assertEquals(BigDecimal.valueOf(333), AnkhEconomy.get(testPlayerA, null));

    AnkhEconomy.add(testPlayerA, null, BigDecimal.valueOf(0.0001));
    Assertions.assertEquals(
        AnkhEconomy.render(testPlayerA, null, new BigDecimal("333.0001")),
        AnkhEconomy.render(testPlayerA, null, AnkhEconomy.get(testPlayerA, null))
    );
  }
}
