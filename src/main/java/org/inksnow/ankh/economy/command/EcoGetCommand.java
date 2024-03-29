package org.inksnow.ankh.economy.command;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.inksnow.ankh.economy.Platform;
import org.inksnow.ankh.economy.api.AnkhEconomy;

@RequiredArgsConstructor
public class EcoGetCommand<P> {

  private final Platform<P> platform;

  public void execute(P sender, String[] args) {
    if (sender != null && !platform.checkPermission(sender, "ankh.economy.command.get")) {
      platform.sendMessage(sender, "You do not have permission to use this command");
      return;
    }
    if (args.length != 1 && args.length != 2) {
      platform.sendMessage(sender, "Usage: /ecoget <player> [currency]");
      return;
    }
    P player = platform.nameToPlayer(args[0]);
    if (player == null) {
      platform.sendMessage(sender, "Player " + args[0] + " not online");
      return;
    }
    String currency = (args.length == 2) ? args[1] : null;
    BigDecimal balance = AnkhEconomy.get(player, currency);

    StringBuilder builder = new StringBuilder();
    builder.append("Balance of ").append(args[0]);
    if (currency != null) {
      builder.append(" in ").append(currency);
    }
    builder.append(": ").append(AnkhEconomy.render(player, currency, balance));

    platform.sendMessage(sender, builder.toString());
  }
}
