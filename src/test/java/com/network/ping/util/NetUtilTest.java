package com.network.ping.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NetUtilTest {
    @Mock
    private DatagramSocket socket;

    @Test
    public void send() throws IOException {
        NetUtil.send(new byte[0], socket, null, 10);
        verify(socket).send(any(DatagramPacket.class));
    }

    @Test(expected = NullPointerException.class)
    public void sendNullDataThrowsNPE() throws IOException {
        NetUtil.send(null, socket, null, 10);
    }

    @Test
    public void receive() throws IOException {
        NetUtil.receive(new byte[1], socket, null, 10);
        verify(socket).receive(any(DatagramPacket.class));
    }

    @Test
    public void receiveWithoutAddress() throws IOException {
        NetUtil.receive(new byte[1], socket);
        verify(socket).receive(any(DatagramPacket.class));
    }

    @Test
    public void receiveWithTimeout() throws IOException {
        int timeout = 100;
        NetUtil.receive(new byte[1], socket, null, 10, timeout);
        verify(socket).receive(any(DatagramPacket.class));
        verify(socket).setSoTimeout(eq(timeout));
        verify(socket).setSoTimeout(eq(0));
    }

    @Test(expected = SocketTimeoutException.class)
    public void receiveWithTimeoutThrowExceptionUp() throws IOException {
        doThrow(SocketTimeoutException.class).
                when(socket).receive(any(DatagramPacket.class));
        NetUtil.receive(new byte[1], socket, null, 10, 100);
    }
}