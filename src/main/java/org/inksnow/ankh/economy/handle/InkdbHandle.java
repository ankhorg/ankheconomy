package org.inksnow.ankh.economy.handle;

import bot.inker.inkos.InkDbNative;
import bot.inker.inkos.backend.IBackend;
import bot.inker.inkos.backend.lmdb.NativeLmdb;
import bot.inker.inkos.backend.lmdb.NativeLmdbTxn;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.java.Log;
import org.bukkit.OfflinePlayer;
import org.inksnow.ankh.economy.api.AtomicEconomyHandle;
import org.inksnow.ankh.economy.api.EconomyHandle;
import org.inksnow.ankh.economy.config.CurrencyConfig;
import org.inksnow.ankh.economy.util.FastBigDecimalUtil;
import org.inksnow.ankh.economy.util.FastUuidUtil;

@Log
public class InkdbHandle implements EconomyHandle, AtomicEconomyHandle {

  private final IBackend inkOs;
  private final byte[] prefix;
  private CurrencyConfig config;

  private InkdbHandle(CurrencyConfig config, IBackend inkOs) {
    this.config = config;
    this.inkOs = inkOs;

    this.prefix = config.getProperties().get("prefix")
        .getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public CurrencyConfig config() {
    return config;
  }

  @Override
  public void shutdown() {
    try {
      inkOs.close();
    } catch (IOException e) {
      //
    }
  }

  @Override
  public boolean canReload(CurrencyConfig economyConfig) {
    return this.config.getType().equals(economyConfig.getType())
        && this.config.getProperties().get("storage")
        .equals(economyConfig.getProperties().get("storage"))
        && this.config.getProperties().get("prefix")
        .equals(economyConfig.getProperties().get("prefix"));
  }

  @Override
  public void reload(CurrencyConfig economyConfig) {
    this.config = economyConfig;
  }

  @Override
  public boolean hasAccount(OfflinePlayer player) {
    byte[] key = FastUuidUtil.uuidToBytesKey(player.getUniqueId(), this.prefix);
    return inkOs.get(key) != null;
  }

  @Override
  public BigDecimal get(OfflinePlayer player) {
    byte[] key = FastUuidUtil.uuidToBytesKey(player.getUniqueId(), this.prefix);
    byte[] amountBytes = inkOs.get(key);
    return amountBytes == null ? BigDecimal.ZERO : FastBigDecimalUtil.fromBytes(amountBytes);
  }

  @Override
  public void set(OfflinePlayer player, BigDecimal amount) {
    byte[] key = FastUuidUtil.uuidToBytesKey(player.getUniqueId(), this.prefix);
    inkOs.put(key, FastBigDecimalUtil.toBytes(amount));
  }

  @Override
  public boolean compareAndSet(OfflinePlayer player, BigDecimal expect, BigDecimal update) {
    UUID id = player.getUniqueId();
    byte[] keyBytes = FastUuidUtil.uuidToBytesKey(id, this.prefix);
    byte[] expectBytes = FastBigDecimalUtil.toBytes(expect);
    byte[] updateBytes = FastBigDecimalUtil.toBytes(update);

    if (inkOs instanceof NativeLmdb) {
      try (NativeLmdbTxn txn = ((NativeLmdb) inkOs).beginWriteTxn()) {
        byte[] currentValue = txn.get(keyBytes);
        if (currentValue == null) {
          currentValue = FastBigDecimalUtil.BYTES_ZERO;
        }
        if (Arrays.equals(currentValue, expectBytes)) {
          txn.put(keyBytes, updateBytes);
          txn.commit();
          return true;
        } else {
          txn.abort();
          return false;
        }
      }
    } else {
      byte[] currentValue = inkOs.get(keyBytes);
      if (currentValue == null) {
        currentValue = FastBigDecimalUtil.BYTES_ZERO;
      }
      if (Arrays.equals(currentValue, expectBytes)) {
        inkOs.put(keyBytes, updateBytes);
        return true;
      } else {
        return false;
      }
    }
  }

  public abstract static class Factory implements EconomyHandle.Factory {

    public abstract IBackend createBackend(String storageDirectory) throws IOException;

    @Override
    public EconomyHandle create(CurrencyConfig currencyConfig)
        throws IOException {
      return new InkdbHandle(currencyConfig,
          createBackend(currencyConfig.getProperties().get("storage")));
    }
  }

  public static class LeveldbFactory extends Factory {

    @Override
    public String name() {
      return "leveldb";
    }

    @Override
    public IBackend createBackend(String storageDirectory) throws IOException {
      return InkDbNative.createLeveldb(storageDirectory);
    }
  }

  public static class InkdbFactory extends Factory {

    @Override
    public String name() {
      return "inkdb";
    }

    @Override
    public IBackend createBackend(String storageDirectory) throws IOException {
      return InkDbNative.createLmdb(storageDirectory);
    }
  }
}
