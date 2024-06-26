package org.inksnow.ankh.economy.bukkit.luckperms;

import java.math.BigDecimal;
import lombok.extern.java.Log;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.inksnow.ankh.economy.api.EconomyHandle;
import org.inksnow.ankh.economy.config.CurrencyConfig;
import org.jetbrains.annotations.NotNull;

@Log
public class LuckPermsMetadataHandle implements EconomyHandle {

  private final String metadataKey;
  private CurrencyConfig config;


  public LuckPermsMetadataHandle(CurrencyConfig config) {
    this.config = config;
    this.metadataKey = config.getProperties().get("metadataKey");
  }

  @Override
  public @NotNull CurrencyConfig config() {
    return config;
  }

  @Override
  public void shutdown() {
    //
  }

  @Override
  public boolean canReload(@NotNull CurrencyConfig economyConfig) {
    return config.getType().equals(economyConfig.getType())
        && config.getProperties()
        .get("metadataKey")
        .equals(economyConfig.getProperties().get("metadataKey"));
  }

  @Override
  public void reload(@NotNull CurrencyConfig economyConfig) {
    config = economyConfig;
  }

  @Override
  public boolean hasAccount(@NotNull OfflinePlayer offlinePlayer) {
    LuckPerms luckPerms = LuckPermsProvider.get();
    PlayerAdapter<Player> playerAdapter = luckPerms.getPlayerAdapter(Player.class);
    if (!offlinePlayer.isOnline()) {
      logger.warning("Player is offline, use luckperms backend will be dangerous and cause "
          + "serious lag, it can only be use in develop environment");
      User user = luckPerms.getUserManager()
          .loadUser(offlinePlayer.getUniqueId())
          .join();
      return user.data()
          .toCollection()
          .stream()
          .anyMatch(it ->
              it instanceof MetaNode
                  && ((MetaNode) it).getMetaKey().equals(metadataKey)
          );
    }
    Player player = (Player) offlinePlayer;
    CachedMetaData metaData = playerAdapter.getMetaData(player);
    return metaData.getMetaValue(metadataKey) != null;
  }

  @Override
  public @NotNull BigDecimal get(@NotNull OfflinePlayer offlinePlayer) {
    LuckPerms luckPerms = LuckPermsProvider.get();
    PlayerAdapter<Player> playerAdapter = luckPerms.getPlayerAdapter(Player.class);
    String valueStr;
    if (offlinePlayer.isOnline()) {
      Player player = (Player) offlinePlayer;
      CachedMetaData metaData = playerAdapter.getMetaData(player);
      valueStr = metaData.getMetaValue(metadataKey);
    } else {
      logger.warning("Player is offline, use luckperms backend will be dangerous and cause "
          + "serious lag, it can only be use in develop environment");
      User user = luckPerms.getUserManager()
          .loadUser(offlinePlayer.getUniqueId())
          .join();
      MetaNode metaNode = (MetaNode) user.data()
          .toCollection()
          .stream()
          .filter(it -> it instanceof MetaNode && ((MetaNode) it).getMetaKey().equals(metadataKey))
          .findAny()
          .orElse(null);
      valueStr = metaNode.getMetaValue();
    }
    return valueStr == null ? BigDecimal.ZERO : new BigDecimal(valueStr);
  }

  @Override
  public void set(@NotNull OfflinePlayer offlinePlayer, @NotNull BigDecimal amount) {
    LuckPerms luckPerms = LuckPermsProvider.get();
    PlayerAdapter<Player> playerAdapter = luckPerms.getPlayerAdapter(Player.class);
    User user;
    if (offlinePlayer.isOnline()) {
      Player player = (Player) offlinePlayer;
      user = playerAdapter.getUser(player);
    } else {
      logger.warning("Player is offline, use luckperms backend will be dangerous and cause "
          + "serious lag, it can only be use in develop environment");
      user = luckPerms.getUserManager()
          .loadUser(offlinePlayer.getUniqueId())
          .join();
    }
    user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals(metadataKey)));
    user.data().add(MetaNode.builder().key(metadataKey).value(amount.toPlainString()).build());
    luckPerms.getUserManager().saveUser(user);
  }
}
