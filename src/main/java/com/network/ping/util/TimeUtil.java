package com.network.ping.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtil {
    public static long msOfMinute() {
        return System.currentTimeMillis() & 65535;
    }
}
