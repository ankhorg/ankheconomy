package org.inksnow.ankh.economy.bukkit.util;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;

@UtilityClass
@Accessors(fluent = true)
public class ExternalUtil {
  @Getter(lazy = true)
  private final boolean hasVault = hasClass("net.milkbowl.vault.economy.Economy");

  @Getter(lazy = true)
  private final boolean hasPlaceHolderApi = hasClass("me.clip.placeholderapi.PlaceholderAPI");

  @Getter(lazy = true)
  private final boolean hasLuckPerms = hasClass("net.luckperms.api.LuckPerms");

  public static boolean hasClass(String className) {
    try {
      Class.forName(className);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
