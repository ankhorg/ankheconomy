package org.inksnow.ankh.economy.command;

import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.inksnow.ankh.economy.AnkhEconomyImpl;
import org.inksnow.ankh.economy.api.AnkhEconomy;

@RequiredArgsConstructor
public class EcoReloadCommand {

  private static final Logger logger = Logger.getLogger("AnkhEconomy");

  public void execute(CommandSender sender, String[] args) {
    if (sender != null && !sender.hasPermission("ankh.economy.command.pay")) {
      sender.sendMessage("You do not have permission to use this command");
      return;
    }
    if (args.length != 0) {
      sender.sendMessage("Usage: /ecoreload");
      return;
    }
    try {
      ((AnkhEconomyImpl) AnkhEconomy.instance()).load();
      sender.sendMessage("Success");
    } catch (Exception e) {
      sender.sendMessage("Failed to reload economy data, see console for more information");
      logger.warning("Failed to reload economy data");
    }
  }
}
