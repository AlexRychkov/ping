package com.network.ping.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtil {
    public static int from(byte[] val) {
        String strVal = new String(val).trim();
        int result = 0;
        try {
            result = Integer.valueOf(strVal);
        } catch (NumberFormatException ignore) {
        }
        return result;
    }
}
