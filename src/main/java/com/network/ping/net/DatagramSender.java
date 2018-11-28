package com.network.ping.net;

import com.network.ping.entity.RTTData;
import com.network.ping.util.NetUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static com.network.ping.util.TimeUtil.msOfMinute;

public class DatagramSender implements Sender {
    private static final int TIMEOUT = 3000;
    private DatagramSocket socket;
    private InetAddress remoteAddress;
    private int remotePort;

    private DatagramSender(DatagramSocket socket, InetAddress remoteAddress, int remotePort) {
        this.socket = socket;
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
    }

    static DatagramSender create(InetAddress remoteAddress, int localPort, int remotePort, int messageSize) {
        DatagramSender sender;
        try {
            DatagramSocket socket = new DatagramSocket(localPort);
            recognizeReceiver(socket, remoteAddress, remotePort, messageSize);
            sender = new DatagramSender(socket, remoteAddress, remotePort);
        } catch (SocketException e) {
            String message = String.format("Socket error was occurred: %s", e.getMessage());
            System.out.println(message);
            throw new IllegalStateException();
        }
        return sender;
    }

    private static void recognizeReceiver(DatagramSocket socket, InetAddress remoteAddress, int remotePort, int messageSize) {
        String msgSize = String.valueOf(messageSize);
        byte[] data = msgSize.getBytes();
        try {
            NetUtil.send(data, socket, remoteAddress, remotePort);
            NetUtil.receive(data, socket, remoteAddress, remotePort, TIMEOUT);
        } catch (IOException e) {
            String message = String.format("Cannot recognize remote address %s:%d cause of: %s", remoteAddress, remotePort, e.getMessage());
            System.out.println(message);
            throw new IllegalStateException();
        }
        String respondedSize = new String(data);
        if (!respondedSize.equals(msgSize)) {
            System.out.println("Remote computer don't expecting connections");
            throw new IllegalStateException();
        }
    }

    @Override
    public void send(byte[] data) {
        try {
            NetUtil.send(data, socket, remoteAddress, remotePort);
        } catch (IOException e) {
            System.out.println(String.format("Error was occurred during sending a message: %s", e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public RTTData receive(int messageSize) {
        byte[] data = new byte[messageSize];
        DatagramPacket response = null;
        long receiveTime;
        try {
            response = NetUtil.receive(data, socket, remoteAddress, remotePort);
            receiveTime = msOfMinute();
        } catch (IOException e) {
            receiveTime = 0L;
        }
        if (response != null) {
            return new RTTData(receiveTime, response.getData());
        } else {
            return null;
        }
    }
}
