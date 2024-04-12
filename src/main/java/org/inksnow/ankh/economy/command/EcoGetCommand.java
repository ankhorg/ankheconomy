package org.inksnow.ankh.economy.command;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inksnow.ankh.economy.api.AnkhEconomy;

@RequiredArgsConstructor
public class EcoGetCommand {

  public void execute(Player sender, String[] args) {
    if (sender != null && !sender.hasPermission("ankh.economy.command.get")) {
      sender.sendMessage("You do not have permission to use this command");
      return;
    }
    if (args.length != 1 && args.length != 2) {
      sender.sendMessage("Usage: /ecoget <player> [currency]");
      return;
    }
    Player player = Bukkit.getPlayer(args[0]);
    if (player == null) {
      sender.sendMessage("Player " + args[0] + " not online");
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

    sender.sendMessage(builder.toString());
  }
}
