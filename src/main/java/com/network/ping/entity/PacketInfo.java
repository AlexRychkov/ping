package com.network.ping.entity;

import lombok.Getter;

@Getter
public class PacketInfo {
    public static PacketInfo EMPTY_PACKET = new PacketInfo(true);
    private final int number;
    private final long pitcherSendTime;
    private final long catcherReceiveTime;
    private final long pitcherReceiveTime;
    private final boolean isEmpty;

    public PacketInfo(int number, long pitcherSendTime, long catcherReceiveTime, long pitcherReceiveTime) {
        this.number = number;
        this.pitcherSendTime = pitcherSendTime;
        this.catcherReceiveTime = catcherReceiveTime;
        this.pitcherReceiveTime = pitcherReceiveTime;
        this.isEmpty = false;
    }

    private PacketInfo(boolean isEmpty) {
        this.number = 0;
        this.pitcherSendTime = 0L;
        this.catcherReceiveTime = 0L;
        this.pitcherReceiveTime = 0L;
        this.isEmpty = isEmpty;
    }
}