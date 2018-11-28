package com.network.ping.net;

import com.network.ping.entity.RTTData;

public interface Sender {
    void send(byte[] data);

    RTTData receive(int messageSize);
}
