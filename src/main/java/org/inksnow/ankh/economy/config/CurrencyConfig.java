package org.inksnow.ankh.economy.config;

import java.util.Map;
import lombok.Data;

@Data
public class CurrencyConfig {

  private String name;
  private String type;
  private Map<String, String> properties;
}
