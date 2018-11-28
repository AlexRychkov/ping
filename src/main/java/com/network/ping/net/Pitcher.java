package com.network.ping.net;

import com.network.ping.entity.PacketInfo;
import com.network.ping.entity.RTTData;
import com.network.ping.mapper.PacketMapper;
import com.network.ping.statistics.StatisticsHolder;

import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.network.ping.util.TimeUtil.msOfMinute;

public class Pitcher implements Runnable {
    private Sender sender;
    private int localPort;
    private int remotePort;
    private final int messagePerSecond;
    private final int messageSize;
    private final InetAddress remoteAddress;
    private final StatisticsHolder statisticsHolder;

    public Pitcher(InetAddress remoteAddress, int localPort, int remotePort, int messagePerSecond, int messageSize) {
        this.localPort = localPort;
        this.remotePort = remotePort;
        this.messagePerSecond = messagePerSecond;
        this.messageSize = messageSize;
        this.statisticsHolder = new StatisticsHolder();
        this.remoteAddress = remoteAddress;
    }

    @Override
    public void run() {
        sender = DatagramSender.create(remoteAddress, localPort, remotePort, messageSize);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(this::repeatablePrintAndPitch, 0, 1, TimeUnit.SECONDS);
        refreshStatistics();
    }

    private void repeatablePrintAndPitch() {
        if (statisticsHolder.getTotalMessages() != 0) {
            System.out.println(statisticsHolder);
        }
        statisticsHolder.reset(msOfMinute());
        for (int messageCounter = 0; messageCounter < messagePerSecond; messageCounter++) {
            byte[] bytes = PacketMapper.map(statisticsHolder.nextMessageNumber(), messageSize);
            sender.send(bytes);
        }
    }

    private void refreshStatistics() {
        while (true) {
            RTTData rttData = sender.receive(messageSize);
            PacketInfo packetInfo = PacketMapper.map(rttData);
            statisticsHolder.putStatistic(packetInfo);
        }
    }
}