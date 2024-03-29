package org.inksnow.ankh.economy.command;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.inksnow.ankh.economy.Platform;
import org.inksnow.ankh.economy.api.AnkhEconomy;

@RequiredArgsConstructor
public class PayCommand<P> {

  private final Platform<P> platform;

  public void execute(P sender, String[] args) {
    if (sender == null) {
      platform.sendMessage(sender, "This command can only be used by players");
      return;
    }
    if (!platform.checkPermission(sender, "ankh.economy.command.pay")) {
      platform.sendMessage(sender, "You do not have permission to use this command");
      return;
    }
    if (args.length != 2 && args.length != 3) {
      platform.sendMessage(sender, "Usage: /pay <player> <balance> [currency]");
      return;
    }
    P player = platform.nameToPlayer(args[0]);
    if (player == null) {
      platform.sendMessage(sender, "Player " + args[0] + " not online");
      return;
    }
    String currency = (args.length == 3) ? args[2] : null;

    BigDecimal amount;
    try {
      amount = new BigDecimal(args[1]);
    } catch (NumberFormatException e) {
      platform.sendMessage(sender, "Invalid amount: " + args[1]);
      return;
    }

    if (AnkhEconomy.subtract(sender, currency, amount)) {
      AnkhEconomy.add(player, currency, amount);
      platform.sendMessage(sender, "Success");
    } else {
      platform.sendMessage(sender, "balance not enough balance");
    }
  }
}
