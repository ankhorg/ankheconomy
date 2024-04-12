package org.inksnow.ankh.economy.bukkit;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.inksnow.ankh.economy.AnkhEconomyImpl;
import org.inksnow.ankh.economy.bukkit.placeholder.AnkhEconomyExpansion;
import org.inksnow.ankh.economy.bukkit.vault.VaultEconomyBind;
import org.inksnow.ankh.economy.command.BalanceCommand;
import org.inksnow.ankh.economy.command.EcoAddCommand;
import org.inksnow.ankh.economy.command.EcoGetCommand;
import org.inksnow.ankh.economy.command.EcoReloadCommand;
import org.inksnow.ankh.economy.command.EcoSetCommand;
import org.inksnow.ankh.economy.command.EcoTakeCommand;
import org.inksnow.ankh.economy.command.PayCommand;

public class AnkhEconomyPlugin extends JavaPlugin {
  private static final Logger logger = Logger.getLogger("AnkhEconomy");

  private AnkhEconomyImpl ankhEconomy;

  public AnkhEconomyPlugin() {
  }

  public AnkhEconomyPlugin(JavaPluginLoader loader,
      PluginDescriptionFile description, File dataFolder,
      File file) {
    super(loader, description, dataFolder, file);
  }

  @Override
  public void onLoad() {
    this.ankhEconomy = new AnkhEconomyImpl();
    try {
      this.ankhEconomy.load();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void onEnable() {
    if (ExternalUtil.hasVault()) {
      setupVault();
    }
    if (ExternalUtil.hasPlaceHolderApi()) {
      setupPlaceHolder();
    }
  }

  private void setupVault() {
    new VaultEconomyBind(ankhEconomy).register(this);
  }

  private void setupPlaceHolder() {
    new AnkhEconomyExpansion(this).register();
  }

  @Override
  public void onDisable() {

  }

  @Override
  public boolean onCommand(
      CommandSender commandSender,
      Command command,
      String label,
      String[] args) {
    Player sender = (commandSender instanceof Player) ? (Player) commandSender : null;
    switch (label) {
      case "balance": {
        new BalanceCommand().execute(sender, args);
        return true;
      }
      case "pay": {
        new PayCommand().execute(sender, args);
        return true;
      }
      case "ecoget": {
        new EcoGetCommand().execute(sender, args);
        return true;
      }
      case "ecoset": {
        new EcoSetCommand().execute(sender, args);
        return true;
      }
      case "ecoadd": {
        new EcoAddCommand().execute(sender, args);
        return true;
      }
      case "ecotake": {
        new EcoTakeCommand().execute(sender, args);
        return true;
      }
      case "ecoreload": {
        new EcoReloadCommand().execute(sender, args);
        return true;
      }
      default: {
        return false;
      }
    }
  }
}
