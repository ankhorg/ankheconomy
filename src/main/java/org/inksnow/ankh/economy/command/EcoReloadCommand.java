package org.inksnow.ankh.economy.command;

import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.inksnow.ankh.economy.AnkhEconomyImpl;
import org.inksnow.ankh.economy.Platform;
import org.inksnow.ankh.economy.api.AnkhEconomy;

@RequiredArgsConstructor
public class EcoReloadCommand<P> {

  private static final Logger logger = Logger.getLogger("AnkhEconomy");
  private final Platform<P> platform;

  public void execute(P sender, String[] args) {
    if (sender != null && !platform.checkPermission(sender, "ankh.economy.command.pay")) {
      platform.sendMessage(sender, "You do not have permission to use this command");
      return;
    }
    if (args.length != 0) {
      platform.sendMessage(sender, "Usage: /ecoreload");
      return;
    }
    try {
      ((AnkhEconomyImpl<?>) AnkhEconomy.instance()).load();
      platform.sendMessage(sender, "Success");
    } catch (Exception e) {
      platform.sendMessage(sender,
          "Failed to reload economy data, see console for more information");
      logger.warning("Failed to reload economy data");
    }
  }
}
