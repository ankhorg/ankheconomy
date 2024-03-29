package org.inksnow.ankh.economy.bukkit;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.inksnow.ankh.economy.AnkhEconomyImpl;
import org.inksnow.ankh.economy.Platform;
import org.inksnow.ankh.economy.bukkit.placeholder.AnkhEconomyExpansion;
import org.inksnow.ankh.economy.bukkit.util.ExternalUtil;
import org.inksnow.ankh.economy.bukkit.vault.VaultEconomyBind;
import org.inksnow.ankh.economy.command.BalanceCommand;
import org.inksnow.ankh.economy.command.EcoAddCommand;
import org.inksnow.ankh.economy.command.EcoGetCommand;
import org.inksnow.ankh.economy.command.EcoReloadCommand;
import org.inksnow.ankh.economy.command.EcoSetCommand;
import org.inksnow.ankh.economy.command.EcoTakeCommand;
import org.inksnow.ankh.economy.command.PayCommand;

public class AnkhEconomyPlugin extends JavaPlugin implements Platform<OfflinePlayer> {
  private static final Logger logger = Logger.getLogger("AnkhEconomy");

  private AnkhEconomyImpl<OfflinePlayer> ankhEconomy;

  @Override
  public void onLoad() {
    this.ankhEconomy = new AnkhEconomyImpl<>(this);
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
    OfflinePlayer sender = (commandSender instanceof Player) ? (Player) commandSender : null;
    switch (label) {
      case "balance": {
        new BalanceCommand<>(this).execute(sender, args);
        return true;
      }
      case "pay": {
        new PayCommand<>(this).execute(sender, args);
        return true;
      }
      case "ecoget": {
        new EcoGetCommand<>(this).execute(sender, args);
        return true;
      }
      case "ecoset": {
        new EcoSetCommand<>(this).execute(sender, args);
        return true;
      }
      case "ecoadd": {
        new EcoAddCommand<>(this).execute(sender, args);
        return true;
      }
      case "ecotake": {
        new EcoTakeCommand<>(this).execute(sender, args);
        return true;
      }
      case "ecoreload": {
        new EcoReloadCommand<>(this).execute(sender, args);
        return true;
      }
      default: {
        return false;
      }
    }
  }

  @Override
  public String pluginConfigDirectory() {
    return "./plugins/AnkhEconomy";
  }

  @Override
  public UUID playerUuid(OfflinePlayer player) {
    return player.getUniqueId();
  }

  @Override
  public OfflinePlayer nameToPlayer(String playerName) {
    return getServer().getOfflinePlayer(playerName);
  }

  @Override
  public OfflinePlayer uuidToPlayer(UUID playerUuid) {
    return getServer().getOfflinePlayer(playerUuid);
  }

  @Override
  public boolean checkPermission(OfflinePlayer player, String permission) {
    if (player instanceof Player) {
      return ((Player) player).hasPermission(permission);
    } else {
      return false;
    }
  }

  @Override
  public void sendMessage(OfflinePlayer player, String message) {
    if (player == null) {
      Bukkit.getConsoleSender().sendMessage(message);
    } else if (player instanceof Player) {
      ((Player) player).sendMessage(message);
    }
  }
}
