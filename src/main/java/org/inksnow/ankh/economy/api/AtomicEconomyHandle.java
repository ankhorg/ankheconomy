package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface AtomicEconomyHandle extends EconomyHandle {

  /**
   * Atomically sets the value to the given updated value if the current value {@code ==} the
   * expected value.
   *
   * @param expect the expected value
   * @param update the new value
   * @return {@code true} if successful. False return indicates that the actual value was not equal
   *         to the expected value.
   */
  boolean compareAndSet(@NonNull OfflinePlayer player, @NonNull BigDecimal expect,
      @NonNull BigDecimal update);
}
