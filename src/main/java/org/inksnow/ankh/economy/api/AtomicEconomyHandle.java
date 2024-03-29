package org.inksnow.ankh.economy.api;

import java.math.BigDecimal;

public interface AtomicEconomyHandle<P> extends EconomyHandle<P> {

  /**
   * Atomically sets the value to the given updated value if the current value {@code ==} the
   * expected value.
   *
   * @param expect the expected value
   * @param update the new value
   * @return {@code true} if successful. False return indicates that the actual value was not equal
   *     to the expected value.
   */
  boolean compareAndSet(P player, BigDecimal expect, BigDecimal update);
}
