package com.network.ping.net;

import java.io.Closeable;

public interface Receiver extends Closeable {
    void receiveAndSend();
}
