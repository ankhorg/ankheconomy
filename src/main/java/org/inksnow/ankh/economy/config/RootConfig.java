package org.inksnow.ankh.economy.config;

import java.util.Map;
import lombok.Data;

@Data
public class RootConfig {

  private String defaultCurrency;
  private VaultConfig vault;
  private Map<String, CurrencyConfig> currencies;
}
