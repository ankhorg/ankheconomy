package org.inksnow.ankh.economy.command;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.inksnow.ankh.economy.api.AnkhEconomy;

@RequiredArgsConstructor
public class BalanceCommand {

  public void execute(Player sender, String[] args) {
    if (sender == null) {
      sender.sendMessage("This command can only be used by players");
      return;
    }
    if (!sender.hasPermission("ankh.economy.command.balance")) {
      sender.sendMessage("You do not have permission to use this command");
      return;
    }
    if (args.length != 0 && args.length != 1) {
      sender.sendMessage("Usage: /balance [currency]");
      return;
    }
    String currency = (args.length == 1) ? args[0] : null;
    BigDecimal balance = AnkhEconomy.get(sender, currency);

    StringBuilder builder = new StringBuilder();
    builder.append("Balance");
    if (currency != null) {
      builder.append(" in ").append(currency);
    }
    builder.append(": ").append(AnkhEconomy.render(sender, currency, balance));

    sender.sendMessage(builder.toString());
  }
}
