/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Entity.Configuration;
import Log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketThread extends Thread {

    private Socket mSocket;
    private boolean mRunning;
    private Configuration mConfig;
    private InputStream mIn;
    private OutputStream mOut;

    private final static int TIMEOUT = 10000;
    private final static int MAX_TRY_COUNT = 3;

    private final Object mLock = new Object();

    public SocketThread(Socket socket, int serverPort) {
        mSocket = socket;
        mRunning = true;
        mConfig = new Configuration(serverPort);

    }

    @Override
    public void run() {
        try {

            mIn = mSocket.getInputStream();
            mOut = mSocket.getOutputStream();
            int tries = 0;
            while (mRunning) {
                String response = waitForResponse(TIMEOUT);
                if (response != null) {
                    System.out.println(response);
                    tries = 0;
                } else {
                    System.out.print("null");
                    tries++;
                }

                if (tries >= MAX_TRY_COUNT) {
                    mRunning = false;
                }
            }
            mIn.close();
            mOut.close();
        } catch (Exception e) {
            mRunning = false;
            Log.writeException("SocketThread - stream", e);
        }
        try {
            mSocket.close();
        } catch (Exception e) {
            mRunning = false;
            Log.writeException("Socket thread - socket", e);
        }
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void close() {
        mRunning = false;
    }

    public int getServePort() {
        return mConfig.getServerPort();
    }

    private String waitForResponse(long timeout) {
        try {
            byte buffer[] = new byte[1024];
            int length = 0;
            long start = System.currentTimeMillis();
            while (mRunning && (System.currentTimeMillis() - start < timeout)) {
                if (mIn.available() == 0) {
                    Thread.sleep(10);
                } else {
                    StringBuilder strBuilder = new StringBuilder();
                    while (mIn.available() > 0) {
                        length = mIn.read(buffer);
                        strBuilder.append(new String(buffer, StandardCharsets.UTF_8));
                    }
                    return strBuilder.toString();
                }
            }
        } catch (IOException | InterruptedException e) {
            Log.writeException("SocketThread - waitForResponse", e);
            return null;
        }
        return null;
    }

    public void sendData(byte[] data) {
        synchronized (mLock) {
            if (!mSocket.isClosed()) {
                try {
                    mOut.write(data);
                    mOut.flush();
                } catch (IOException e) {
                    Log.writeException("SocketThread - sendData", e);
                }
            }
        }
    }


}
