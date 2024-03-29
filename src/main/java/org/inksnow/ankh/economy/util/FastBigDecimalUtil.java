package org.inksnow.ankh.economy.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FastBigDecimalUtil {

  public static final byte[] BYTES_ZERO = toBytes(BigDecimal.ZERO);

  public static byte[] toBytes(BigDecimal value) {
    if (value == null) {
      return null;
    }
    byte[] partA = value.unscaledValue().toByteArray();
    byte[] result = new byte[partA.length + 4];
    // scale
    result[0] = (byte) (value.scale() >> 24);
    result[1] = (byte) (value.scale() >> 16);
    result[2] = (byte) (value.scale() >> 8);
    result[3] = (byte) value.scale();
    System.arraycopy(partA, 0, result, 4, partA.length);
    return result;
  }

  public static BigDecimal fromBytes(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    int scale = (bytes[0] << 24) | (bytes[1] << 16) | (bytes[2] << 8) | bytes[3];
    byte[] partA = new byte[bytes.length - 4];
    System.arraycopy(bytes, 4, partA, 0, partA.length);
    return new BigDecimal(new BigInteger(partA), scale);
  }
}
