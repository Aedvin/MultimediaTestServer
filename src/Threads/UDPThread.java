/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Log.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class UDPThread extends Thread {

    private final static int PORT = 49558;
    private final static int MAX_PACKET_SIZE = 1024;

    private boolean mRunning;
    private DatagramSocket mSocket;

    public UDPThread() {
        mRunning = true;
        try {
            mSocket = new DatagramSocket(PORT);
            mSocket.setSoTimeout(500);
        } catch (Exception e) {
            Log.writeException("UDPThread - init", e);
        }
    }

    @Override
    public void run() {
        while (mRunning) {
            byte[] buffer = new byte[MAX_PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, MAX_PACKET_SIZE);
            try {
                mSocket.receive(packet);
                mSocket.send(packet);
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                Log.writeException("UDP Thread - run", e);
            }
        }
        mSocket.close();
    }

    public void close() {
        mRunning = false;
    }

    public boolean isRunning() {
        return mRunning;
    }

}
