package com.network.ping.net;

import com.network.ping.mapper.PacketMapper;
import com.network.ping.util.NetUtil;
import com.network.ping.util.NumberUtil;
import lombok.Value;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class DatagramReceiver implements Receiver, Closeable {
    private final int TIMEOUT = 3000;
    private DatagramSocket socket;
    private InetAddress remoteAddress;
    private int remotePort;
    private int messageSize;

    private DatagramReceiver(DatagramSocket socket, InetAddress remoteAddress, int remotePort, int messageSize) {
        this.socket = socket;
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
        this.messageSize = messageSize;
    }

    static DatagramReceiver create(InetAddress localAddress, int localPort) {
        DatagramReceiver datagramReceiver;
        try {
            DatagramSocket socket = new DatagramSocket(localPort, localAddress);
            Params params = recognizeSender(socket);
            DatagramPacket response = params.getResponse();
            datagramReceiver = new DatagramReceiver(socket, response.getAddress(), response.getPort(), params.getMessageSize());
        } catch (IOException e) {
            String message = String.format("Error was occurred: %s", e.getMessage());
            System.out.println(message);
            throw new IllegalStateException();
        }
        return datagramReceiver;
    }

    private static Params recognizeSender(DatagramSocket socket) throws IOException {
        int MAX_MESSAGE_SIZE_LEN = 4;
        byte[] data = new byte[MAX_MESSAGE_SIZE_LEN];
        DatagramPacket response = null;
        int messageSize = 0;
        while (messageSize == 0) {
            response = NetUtil.receive(data, socket);
            messageSize = NumberUtil.from(data);
        }
        try {
            NetUtil.send(data, socket, response.getAddress(), response.getPort());
        } catch (IOException e) {
            String message = String.format("Cannot recognize any remote address: %s", e.getMessage());
            System.out.println(message);
            throw new IllegalStateException();
        }
        return new Params(messageSize, response);
    }

    @Override
    public void close() {
        socket.close();
    }

    @Override
    public void receiveAndSend() {
        try {
            byte[] data = new byte[messageSize];
            DatagramPacket request = new DatagramPacket(data, data.length, remoteAddress, remotePort);
            socket.setSoTimeout(TIMEOUT);
            socket.receive(request);
            PacketMapper.addCurrentTime(data);
            DatagramPacket response = new DatagramPacket(data, data.length, remoteAddress, remotePort);
            socket.send(response);
        } catch (SocketException e) {
            System.out.println(String.format("Error occurred: %s", e.getMessage()));
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout to receive messages from Pitcher");
            throw new IllegalStateException();
        } catch (IOException e) {
            System.out.println(String.format("Error was occurred: %s", e.getMessage()));
        }
    }

    @Value
    private static class Params {
        private final int messageSize;
        private final DatagramPacket response;
    }
}
