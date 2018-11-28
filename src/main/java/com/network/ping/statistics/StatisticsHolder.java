package com.network.ping.statistics;

import com.network.ping.entity.PacketInfo;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StatisticsHolder {
    private final int SECOND = 1000;
    private int totalMessages;
    private int totalReceivedMessages;
    private int speed;
    private long maxRtt;
    private long sumAtoA;
    private long sumAtoB;
    private long sumBtoA;
    private long previousSecondBeginning;

    public StatisticsHolder() {
        totalMessages = 0;
    }

    public int nextMessageNumber() {
        speed++;
        return ++totalMessages;
    }

    public void reset(long msOfMinute) {
        previousSecondBeginning = msOfMinute;
        speed = 0;
        sumAtoA = 0;
        sumBtoA = 0;
        sumAtoB = 0;
    }

    public void putStatistic(PacketInfo packetInfo) {
        totalReceivedMessages++;
        long rttTime = packetInfo.getPitcherReceiveTime() - packetInfo.getPitcherSendTime();
        if (packetInfo.getPitcherSendTime() >= previousSecondBeginning && packetInfo.getPitcherSendTime() <= previousSecondBeginning + SECOND) {
            sumAtoA += rttTime;
            sumAtoB += packetInfo.getCatcherReceiveTime() - packetInfo.getPitcherSendTime();
            sumBtoA += packetInfo.getPitcherReceiveTime() - packetInfo.getCatcherReceiveTime();
        }
        maxRtt = Long.max(maxRtt, rttTime);
    }

    public int getLostMessagesAmount() {
        return totalMessages - totalReceivedMessages;
    }

    @Override
    public String toString() {
        long avgAtoA = 0;
        long avgAtoB = 0;
        long avgBtoA = 0;
        if (speed != 0) {
            avgAtoA = sumAtoA / speed;
            avgAtoB = sumAtoB / speed;
            avgBtoA = sumBtoA / speed;
        }
        return String.format(
                "time=%s, total=%d, speed=%d, max(RTT)=%d, avg(RTT)=%d, avg(A->B)=%d, avg(B->A)=%d, lost=%d",
                LocalTime.now().toString().substring(0, 8),
                totalMessages,
                speed,
                maxRtt,
                avgAtoA,
                avgAtoB,
                avgBtoA,
                getLostMessagesAmount()
        );
    }
}