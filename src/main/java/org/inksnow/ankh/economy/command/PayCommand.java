package org.inksnow.ankh.economy.command;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inksnow.ankh.economy.api.AnkhEconomy;

@RequiredArgsConstructor
public class PayCommand {

  public void execute(Player sender, String[] args) {
    if (sender == null) {
      sender.sendMessage("This command can only be used by players");
      return;
    }
    if (!sender.hasPermission("ankh.economy.command.pay")) {
      sender.sendMessage("You do not have permission to use this command");
      return;
    }
    if (args.length != 2 && args.length != 3) {
      sender.sendMessage("Usage: /pay <player> <balance> [currency]");
      return;
    }
    Player player = Bukkit.getPlayer(args[0]);
    if (player == null) {
      sender.sendMessage("Player " + args[0] + " not online");
      return;
    }
    String currency = (args.length == 3) ? args[2] : null;

    BigDecimal amount;
    try {
      amount = new BigDecimal(args[1]);
    } catch (NumberFormatException e) {
      sender.sendMessage("Invalid amount: " + args[1]);
      return;
    }

    if (AnkhEconomy.subtract(sender, currency, amount)) {
      AnkhEconomy.add(player, currency, amount);
      sender.sendMessage("Success");
    } else {
      sender.sendMessage("balance not enough balance");
    }
  }
}
