package com.network.ping.statistics;

import com.network.ping.entity.PacketInfo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatisticsHolderTest {
    private StatisticsHolder statisticsHolder;

    @Before
    public void before() {
        statisticsHolder  = new StatisticsHolder();
    }

    @Test
    public void putStatistic() {
        PacketInfo packetInfo = new PacketInfo(24, 100, 200, 300);
        statisticsHolder.putStatistic(packetInfo);
        assertEquals(statisticsHolder.getTotalReceivedMessages(), 1);
        assertEquals(statisticsHolder.getTotalMessages(), 0);
        assertEquals(statisticsHolder.getMaxRtt(), 200);
        assertEquals(statisticsHolder.getSpeed(), 0);
        assertEquals(statisticsHolder.getTotalReceivedMessages(), 1);
        assertEquals(statisticsHolder.getTotalReceivedMessages(), 1);
    }
}