package org.inksnow.ankh.economy;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Logger;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.inksnow.ankh.economy.api.AnkhEconomy;
import org.inksnow.ankh.economy.api.AnkhEconomyApi;
import org.inksnow.ankh.economy.api.AtomicEconomyHandle;
import org.inksnow.ankh.economy.api.BindAllEconomyApi;
import org.inksnow.ankh.economy.api.BindCurrencyEconomyApi;
import org.inksnow.ankh.economy.api.BindPlayerEconomyApi;
import org.inksnow.ankh.economy.api.EconomyHandle;
import org.inksnow.ankh.economy.config.CurrencyConfig;
import org.inksnow.ankh.economy.config.RootConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class AnkhEconomyImpl implements AnkhEconomyApi {

  private static final Logger logger = Logger.getLogger("AnkhEconomy");
  private static final Map<String, EconomyHandle.Factory> handleFactories = new HashMap<>();

  static {
    ServiceLoader.load(EconomyHandle.Factory.class, AnkhEconomyImpl.class.getClassLoader())
        .forEach(factory -> handleFactories.put(factory.name(), factory));
  }

  private final Map<String, EconomyHandle> economyHandles = new HashMap<>();
  private RootConfig config;

  public AnkhEconomyImpl() {
    AnkhEconomy.$internal$actions.setInstance(this);
  }

  public void load() throws IOException {
    reloadConfig();
    loadEconomy();
  }

  public boolean isLoaded() {
    return config != null;
  }

  public RootConfig config() {
    return this.config;
  }

  public EconomyHandle handle(String name) {
    return economyHandles.get(name);
  }

  @SuppressWarnings("VulnerableCodeUsages") // i know this, but spigot use it
  private void reloadConfig() throws IOException {
    Path configDirectory = Paths.get("plugins/AnkhEconomy");
    Files.createDirectories(configDirectory);

    Path configPath = configDirectory.resolve("config.yml");
    if (!Files.exists(configPath)) {
      try (InputStream in = AnkhEconomyImpl.class.getClassLoader()
          .getResourceAsStream("ankh_economy/config.yml")) {
        if (in == null) {
          throw new IOException("no default config found in resource");
        }
        Files.copy(in, configPath);
      }
    }

    try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
      Yaml yaml = new Yaml();
      yaml.setBeanAccess(BeanAccess.FIELD);
      this.config = yaml.loadAs(reader, RootConfig.class);
    }
  }

  private void loadEconomy() throws IOException {
    RootConfig config = this.config;
    if (config == null) {
      throw new IllegalStateException("config still not loaded");
    }

    Iterator<Map.Entry<String, EconomyHandle>> handleIterator = economyHandles.entrySet()
        .iterator();
    while (handleIterator.hasNext()) {
      Map.Entry<String, EconomyHandle> entry = handleIterator.next();
      CurrencyConfig currencyConfig = config.getCurrencies().get(entry.getKey());
      if (currencyConfig == null || !entry.getValue().canReload(currencyConfig)) {
        entry.getValue().shutdown();
        handleIterator.remove();
      }
    }

    Iterator<Map.Entry<String, CurrencyConfig>> currencyConfigIterator = config.getCurrencies()
        .entrySet().iterator();
    while (currencyConfigIterator.hasNext()) {
      Map.Entry<String, CurrencyConfig> entry = currencyConfigIterator.next();
      EconomyHandle handle = economyHandles.get(entry.getKey());
      if (handle == null) {
        economyHandles.put(entry.getKey(), createHandle(entry.getValue()));
      } else {
        handle.reload(entry.getValue());
      }
    }
  }

  private EconomyHandle createHandle(CurrencyConfig economyConfig) throws IOException {
    String type = economyConfig.getType();
    EconomyHandle.Factory factory = handleFactories.get(type);
    return factory.create(economyConfig);
  }

  @Override
  public @NonNull String render(OfflinePlayer player, String currency, @NonNull BigDecimal amount) {
    if (currency == null) {
      currency = config.getDefaultCurrency();
    }
    return amount.stripTrailingZeros().toPlainString();
  }

  @Override
  public @NonNull BigDecimal get(@NonNull OfflinePlayer player, String currency) {
    if (currency == null) {
      currency = config.getDefaultCurrency();
    }
    EconomyHandle handle = handle(currency);
    if (handle == null) {
      throw new IllegalStateException("no economy handle found for currency " + currency);
    }
    return handle.get(player);
  }

  @Override
  public void set(@NonNull OfflinePlayer player, String currency, @NonNull BigDecimal amount) {
    if (currency == null) {
      currency = config.getDefaultCurrency();
    }
    EconomyHandle handle = handle(currency);
    if (handle == null) {
      throw new IllegalStateException("no economy handle found for currency " + currency);
    }
    handle.set(player, amount);
  }

  @Override
  public void add(@NonNull OfflinePlayer player, String currency, @NonNull BigDecimal amount) {
    if (currency == null) {
      currency = config.getDefaultCurrency();
    }
    EconomyHandle handle = handle(currency);
    if (handle == null) {
      throw new IllegalStateException("no economy handle found for currency " + currency);
    }
    if (handle instanceof AtomicEconomyHandle) {
      AtomicEconomyHandle atomicHandle = (AtomicEconomyHandle) handle;
      while (true) {
        BigDecimal currentBalance = handle.get(player);
        BigDecimal balanceAfterProcess = currentBalance.add(amount);
        if (atomicHandle.compareAndSet(player, currentBalance, balanceAfterProcess)) {
          return;
        }
      }
    } else {
      BigDecimal currentBalance = handle.get(player);
      BigDecimal balanceAfterProcess = currentBalance.add(amount);
      handle.set(player, balanceAfterProcess);
    }
  }

  @Override
  public boolean subtract(@NonNull OfflinePlayer player, String currency,
      @NonNull BigDecimal amount) {
    if (currency == null) {
      currency = config.getDefaultCurrency();
    }
    EconomyHandle handle = handle(currency);
    if (handle == null) {
      throw new IllegalStateException("no economy handle found for currency " + currency);
    }
    if (handle instanceof AtomicEconomyHandle) {
      AtomicEconomyHandle atomicHandle = (AtomicEconomyHandle) handle;
      while (true) {
        BigDecimal currentBalance = handle.get(player);
        BigDecimal balanceAfterProcess = currentBalance.subtract(amount);
        if (balanceAfterProcess.compareTo(BigDecimal.ZERO) >= 0) {
          if (atomicHandle.compareAndSet(player, currentBalance, balanceAfterProcess)) {
            return true;
          }
        } else {
          return false;
        }
      }
    } else {
      BigDecimal currentBalance = handle.get(player);
      BigDecimal balanceAfterProcess = currentBalance.subtract(amount);
      if (balanceAfterProcess.compareTo(BigDecimal.ZERO) >= 0) {
        handle.set(player, balanceAfterProcess);
        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  public @NonNull BindPlayerEconomyApi bindPlayer(@NonNull OfflinePlayer player) {
    return new BindPlayerEconomyImpl(this, player);
  }

  @Override
  public @NonNull BindCurrencyEconomyApi bindCurrency(@NonNull String currency) {
    return new BindCurrencyEconomyImpl(this, currency);
  }

  @Override
  public @NonNull BindAllEconomyApi bindAll(@NonNull OfflinePlayer player,
      @NonNull String currency) {
    return new BindAllEconomyImpl(this, player, currency);
  }
}
