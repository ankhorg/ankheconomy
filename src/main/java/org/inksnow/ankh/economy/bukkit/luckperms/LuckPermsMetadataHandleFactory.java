package org.inksnow.ankh.economy.bukkit.luckperms;

import org.inksnow.ankh.economy.api.EconomyHandle;
import org.inksnow.ankh.economy.bukkit.ExternalUtil;
import org.inksnow.ankh.economy.config.CurrencyConfig;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
public class LuckPermsMetadataHandleFactory implements EconomyHandle.Factory {

  @Override
  public @NotNull String name() {
    return "luckperms";
  }

  @Override
  public @NotNull EconomyHandle create(@NotNull CurrencyConfig economyConfig) {
    if (ExternalUtil.hasLuckPerms()) {
      return new LuckPermsMetadataHandle(economyConfig);
    } else {
      throw new IllegalStateException("LuckPerms is not loaded, but you are trying to use "
          + "LuckPerms metadata handle.");
    }
  }
}
