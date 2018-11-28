package com.network.ping.entity;

import lombok.Value;

@Value
public class RTTData {
    private long time;
    private byte[] data;
}
