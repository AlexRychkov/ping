package com.network.ping.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberUtilTest {
    @Test
    public void from() {
        String val = "245";
        assertEquals(245, NumberUtil.from(val.getBytes()));
    }

    @Test
    public void fromIncorrectFormat() {
        String val = "45huh54";
        assertEquals(0, NumberUtil.from(val.getBytes()));
    }

    @Test
    public void fromNegative() {
        String val = "-45";
        assertEquals(-45, NumberUtil.from(val.getBytes()));
    }

    @Test
    public void fromByteArrayWithEMptyElements() {
        byte[] bytes = {52, 53, 0, 0};
        assertEquals(45, NumberUtil.from(bytes));
    }
}