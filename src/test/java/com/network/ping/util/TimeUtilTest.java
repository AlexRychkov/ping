package com.network.ping.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class})
public class TimeUtilTest {
    @Test
    public void msOfMinute() {
        mockStatic(System.class);
        long bigLong = 28475684765L;
        PowerMockito.when(System.currentTimeMillis()).thenReturn(bigLong);
        long time = TimeUtil.msOfMinute();
        assertTrue(time < 65535);
    }
}