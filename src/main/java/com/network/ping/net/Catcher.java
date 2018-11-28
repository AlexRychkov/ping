package com.network.ping.net;

import java.io.IOException;
import java.net.InetAddress;

public class Catcher implements Runnable {
    private int localPort;
    private InetAddress localAddress;

    public Catcher(int localPort, InetAddress localAddress) {
        this.localPort = localPort;
        this.localAddress = localAddress;
    }

    @Override
    public void run() {
        while (true) {
            try (Receiver receiver = DatagramReceiver.create(localAddress, localPort)) {
                while (true) {
                    receiver.receiveAndSend();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException ignored) {
                System.out.println("Going to wait new Pitcher");
            }
        }
    }
}
