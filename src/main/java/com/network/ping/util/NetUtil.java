package com.network.ping.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@UtilityClass
public class NetUtil {
    public static void send(byte[] data, DatagramSocket socket, InetAddress address, int remotePort) throws IOException {
        DatagramPacket request = new DatagramPacket(data, data.length, address, remotePort);
        socket.send(request);
    }

    public static DatagramPacket receive(byte[] data, DatagramSocket socket, InetAddress address, int remotePort) throws IOException {
        DatagramPacket response = new DatagramPacket(data, data.length, address, remotePort);
        socket.receive(response);
        return response;
    }

    public static DatagramPacket receive(byte[] data, DatagramSocket socket) throws IOException {
        DatagramPacket response = new DatagramPacket(data, data.length);
        socket.receive(response);
        return response;
    }

    public static DatagramPacket receive(byte[] data, DatagramSocket socket, InetAddress address, int remotePort, int timeout) throws IOException {
        DatagramPacket response = new DatagramPacket(data, data.length, address, remotePort);
        socket.setSoTimeout(timeout);
        socket.receive(response);
        socket.setSoTimeout(0);
        return response;
    }
}
