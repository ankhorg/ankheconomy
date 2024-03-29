package org.inksnow.ankh.economy.command;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.inksnow.ankh.economy.Platform;
import org.inksnow.ankh.economy.api.AnkhEconomy;

@RequiredArgsConstructor
public class BalanceCommand<P> {

  private final Platform<P> platform;

  public void execute(P sender, String[] args) {
    if (sender == null) {
      platform.sendMessage(sender, "This command can only be used by players");
      return;
    }
    if (!platform.checkPermission(sender, "ankh.economy.command.balance")) {
      platform.sendMessage(sender, "You do not have permission to use this command");
      return;
    }
    if (args.length != 0 && args.length != 1) {
      platform.sendMessage(sender, "Usage: /balance [currency]");
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

    platform.sendMessage(sender, builder.toString());
  }
}
