package Threads;

import Log.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UDPThreadTest {

    UDPThread thread;

    @BeforeEach
    void setUp() {
        thread = new UDPThread();
        thread.start();
        Log.init("threadTest/");
    }

    @AfterEach
    void tearDown() {
        thread.close();
        try {
            thread.join();
        } catch (Exception e) {
            Log.writeException("Thread test", e);
        }
        Log.close();
    }

    @Test
    void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(500);
            Random rnd = new Random();
            byte[] buffer = new byte[512];
            rnd.nextBytes(buffer);
            DatagramPacket packet = new DatagramPacket(buffer, 512);
            packet.setAddress(InetAddress.getByName("localhost"));
            packet.setPort(49558);
            socket.send(packet);

            byte[] bufferSecond = new byte[512];
            DatagramPacket secondPacket = new DatagramPacket(bufferSecond, 512);
            socket.receive(secondPacket);

            Assertions.assertArrayEquals(buffer, bufferSecond, "Ano");
        } catch (SocketTimeoutException e) {
            System.out.println("Client timeout");
        } catch (SocketException e) {
            Log.writeException("Thread test", e);
        } catch (IOException e) {
            Log.writeException("Thread test", e);
        }
    }

}