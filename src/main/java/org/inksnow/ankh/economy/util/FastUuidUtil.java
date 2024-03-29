package org.inksnow.ankh.economy.util;

import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FastUuidUtil {

  public static byte[] uuidToBytes(UUID uuid) {
    byte[] bytes = new byte[16];
    long msb = uuid.getMostSignificantBits();
    long lsb = uuid.getLeastSignificantBits();
    for (int i = 0; i < 8; i++) {
      bytes[i] = (byte) (msb >>> 8 * (7 - i));
      bytes[i + 8] = (byte) (lsb >>> 8 * (7 - i));
    }
    return bytes;
  }

  public static UUID bytesToUuid(byte[] bytes) {
    long msb = 0;
    long lsb = 0;
    for (int i = 0; i < 8; i++) {
      msb = (msb << 8) | (bytes[i] & 0xff);
      lsb = (lsb << 8) | (bytes[i + 8] & 0xff);
    }
    return new UUID(msb, lsb);
  }

  public static byte[] uuidToBytesKey(UUID uuid, byte[] key) {
    int offset = key.length + 1;
    byte[] result = new byte[offset + 16];
    System.arraycopy(key, 0, result, 0, key.length);
    result[key.length] = '.';

    long msb = uuid.getMostSignificantBits();
    long lsb = uuid.getLeastSignificantBits();
    for (int i = 0; i < 8; i++) {
      result[offset + i] = (byte) (msb >>> 8 * (7 - i));
      result[offset + i + 8] = (byte) (lsb >>> 8 * (7 - i));
    }

    return result;
  }
}
