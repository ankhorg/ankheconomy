package org.inksnow.ankh.economy.bukkit.vault;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.inksnow.ankh.economy.AnkhEconomyImpl;
import org.inksnow.ankh.economy.api.AtomicEconomyHandle;
import org.inksnow.ankh.economy.api.EconomyHandle;

public class VaultEconomyBind implements Economy {

  private static final Logger logger = Logger.getLogger("AnkhEconomy");
  private final AnkhEconomyImpl ankhEconomy;

  public VaultEconomyBind(AnkhEconomyImpl ankhEconomy) {
    this.ankhEconomy = ankhEconomy;
  }

  private EconomyHandle defaultHandle() {
    return ankhEconomy.handle(ankhEconomy.config().getDefaultCurrency());
  }

  private OfflinePlayer nameToPlayer(String playerName) {
    Player player = Bukkit.getServer().getPlayer(playerName);
    if (player == null) {
      throw new UnsupportedOperationException("AnkhEconomy does not support use player name as "
          + "account, use UUID instead");
    } else {
      for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
        if (!element.getClassName().startsWith("org.inksnow.ankh.economy.")) {
          logger.warning("AnkhEconomy does not support use player name as account, please "
              + "fix it. Called by " + element);
          break;
        }
      }
      return player;
    }
  }

  @Override
  public boolean isEnabled() {
    return ankhEconomy.isLoaded();
  }

  @Override
  public String getName() {
    return "AnkhEconomy";
  }

  @Override
  public boolean hasBankSupport() {
    return false;
  }

  @Override
  public int fractionalDigits() {
    return ankhEconomy.config()
        .getVault()
        .getFractionalDigits();
  }

  @Override
  public String format(double v) {
    return BigDecimal.valueOf(v).toString();
  }

  @Override
  public String currencyNamePlural() {
    return ankhEconomy.config()
        .getVault()
        .getPlural();
  }

  @Override
  public String currencyNameSingular() {
    return ankhEconomy.config()
        .getVault()
        .getPlural();
  }

  @Override
  public boolean hasAccount(String playerName) {
    return hasAccount(nameToPlayer(playerName));
  }

  @Override
  public boolean hasAccount(OfflinePlayer player) {
    return defaultHandle().hasAccount(player);
  }

  @Override
  public boolean hasAccount(String playerName, String worldName) {
    return hasAccount(nameToPlayer(playerName));
  }

  @Override
  public boolean hasAccount(OfflinePlayer player, String worldName) {
    return hasAccount(player);
  }

  @Override
  public double getBalance(String playerName) {
    return getBalance(nameToPlayer(playerName));
  }

  @Override
  public double getBalance(OfflinePlayer player) {
    return defaultHandle().get(player).doubleValue();
  }

  @Override
  public double getBalance(String playerName, String world) {
    return getBalance(nameToPlayer(playerName));
  }

  @Override
  public double getBalance(OfflinePlayer player, String world) {
    return getBalance(player);
  }

  @Override
  public boolean has(String playerName, double amount) {
    return has(nameToPlayer(playerName), amount);
  }

  @Override
  public boolean has(OfflinePlayer player, double amount) {
    return defaultHandle().get(player).compareTo(BigDecimal.valueOf(amount)) >= 0;
  }

  @Override
  public boolean has(String playerName, String worldName, double amount) {
    return has(nameToPlayer(playerName), amount);
  }

  @Override
  public boolean has(OfflinePlayer player, String worldName, double amount) {
    return has(player, amount);
  }

  @Override
  public EconomyResponse withdrawPlayer(String playerName, double amount) {
    return withdrawPlayer(nameToPlayer(playerName), amount);
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
    EconomyHandle handle = defaultHandle();

    if (handle instanceof AtomicEconomyHandle) {
      AtomicEconomyHandle atomicHandle = (AtomicEconomyHandle) handle;
      while (true) {
        BigDecimal currentBalance = handle.get(player);
        BigDecimal balanceAfterProcess = currentBalance.subtract(BigDecimal.valueOf(amount));
        if (balanceAfterProcess.compareTo(BigDecimal.ZERO) >= 0) {
          if (atomicHandle.compareAndSet(player, currentBalance, balanceAfterProcess)) {
            return new EconomyResponse(
                amount,
                balanceAfterProcess.doubleValue(),
                EconomyResponse.ResponseType.SUCCESS,
                null);
          }
        } else {
          return new EconomyResponse(
              0, currentBalance.doubleValue(),
              EconomyResponse.ResponseType.FAILURE,
              "Insufficient funds"
          );
        }
      }
    } else {
      BigDecimal currentBalance = handle.get(player);
      BigDecimal balanceAfterProcess = currentBalance.subtract(BigDecimal.valueOf(amount));
      if (balanceAfterProcess.compareTo(BigDecimal.ZERO) >= 0) {
        handle.set(player, balanceAfterProcess);
        return new EconomyResponse(
            amount,
            balanceAfterProcess.doubleValue(),
            EconomyResponse.ResponseType.SUCCESS,
            null
        );
      } else {
        return new EconomyResponse(
            0,
            currentBalance.doubleValue(),
            EconomyResponse.ResponseType.FAILURE,
            "Insufficient funds"
        );
      }
    }
  }

  @Override
  public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
    return withdrawPlayer(nameToPlayer(playerName), amount);
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
    return withdrawPlayer(player, amount);
  }

  @Override
  public EconomyResponse depositPlayer(String playerName, double amount) {
    return depositPlayer(nameToPlayer(playerName), amount);
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
    EconomyHandle handle = defaultHandle();

    if (handle instanceof AtomicEconomyHandle) {
      AtomicEconomyHandle atomicHandle = (AtomicEconomyHandle) handle;
      while (true) {
        BigDecimal currentBalance = handle.get(player);
        BigDecimal balanceAfterProcess = currentBalance.add(BigDecimal.valueOf(amount));
        if (atomicHandle.compareAndSet(player, currentBalance, balanceAfterProcess)) {
          return new EconomyResponse(
              amount,
              balanceAfterProcess.doubleValue(),
              EconomyResponse.ResponseType.SUCCESS,
              null
          );
        }
      }
    } else {
      BigDecimal currentBalance = handle.get(player);
      BigDecimal balanceAfterProcess = currentBalance.add(BigDecimal.valueOf(amount));
      handle.set(player, balanceAfterProcess);
      return new EconomyResponse(
          amount,
          balanceAfterProcess.doubleValue(),
          EconomyResponse.ResponseType.SUCCESS,
          null
      );
    }
  }

  @Override
  public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
    return depositPlayer(nameToPlayer(playerName), amount);
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
    return depositPlayer(player, amount);
  }

  @Override
  public EconomyResponse createBank(String name, String player) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse createBank(String name, OfflinePlayer player) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse deleteBank(String name) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse bankBalance(String name) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse bankHas(String name, double amount) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse bankWithdraw(String name, double amount) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse bankDeposit(String name, double amount) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse isBankOwner(String name, String playerName) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse isBankMember(String name, String playerName) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public EconomyResponse isBankMember(String name, OfflinePlayer player) {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public List<String> getBanks() {
    throw new UnsupportedOperationException("bank is not supported");
  }

  @Override
  public boolean createPlayerAccount(String playerName) {
    return createPlayerAccount(nameToPlayer(playerName));
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer player) {
    return true;
  }

  @Override
  public boolean createPlayerAccount(String playerName, String worldName) {
    return createPlayerAccount(nameToPlayer(playerName));
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
    return true;
  }

  public void register(Plugin plugin) {
    Bukkit.getServer()
        .getServicesManager()
        .register(Economy.class, this, plugin, ServicePriority.Normal);

  }
}
