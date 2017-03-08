/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Log.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Random;

public class TCPThread extends Thread {

    private boolean mRunning;
    private ServerSocket mServerSocket;
    private ArrayList<SocketThread> mSocketList;
    private final Object mLock = new Object();
    private ArrayList<Integer> mServerPorts;

    private final static int PORT = 49558;
    private final static int CLIENT_MAX = 20;

    public TCPThread() {
        mRunning = true;
        try {
            mServerSocket = new ServerSocket(PORT);
            mServerSocket.setSoTimeout(100);
            mSocketList = new ArrayList<>(10);
            mServerPorts = new ArrayList<>();
        } catch (IOException e) {
            Log.writeException("TCPThread - init", e);
            mRunning = false;
        }
    }

    @Override
    public void run() {
        while (mRunning) {
            try {
                if (mSocketList.size() <= CLIENT_MAX) {
                    Socket socket = mServerSocket.accept();
                    int serverPort = findServerPort();
                    SocketThread newThread = new SocketThread(socket, serverPort);
                    mSocketList.add(newThread);
                    mServerPorts.add(serverPort);
                    newThread.start();
                }
            } catch (SocketTimeoutException te) {
                checkAliveSockets();
            } catch (Exception e) {
                Log.writeException("TCPThread - run", e);
                mRunning = false;
            }
        }
        deinit();
    }

    public void close() {
        mRunning = false;
    }

    private void deinit() {
        try {
            mServerSocket.close();
            for (int i = 0; i < mSocketList.size(); i++) {
                SocketThread thread = mSocketList.get(i);
                thread.close();
                thread.join();
            }
            mSocketList.clear();
        } catch (Exception e) {
            Log.writeException("TCPThread - deinit", e);
        }
    }

    private void checkAliveSockets() {
        ArrayList<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < mSocketList.size(); i++) {
            SocketThread thread = mSocketList.get(i);
            if (!thread.isRunning()) {
                toRemove.add(i);
                thread.close();
                try {
                    thread.join();
                } catch (Exception e) {
                    Log.writeException("TCPThread - checkAlive", e);
                }
            }
        }

        for (int i = 0; i < toRemove.size(); i++) {
            Integer index = toRemove.get(i);
            mSocketList.remove(index.intValue());
        }
        toRemove.clear();
    }

    private int findServerPort() {
        Random rand = new Random();
        boolean portOk = false;
        int portNum = 0;

        while (!portOk) {
            portNum = rand.nextInt(16383) + 65535;
            if (mServerPorts.size() > 0) {
                boolean arrayOk = true;
                for (int i = 0; i < mServerPorts.size(); i++) {
                    Integer port = mServerPorts.get(i);
                    if (portNum == port) {
                        arrayOk = false;
                        break;
                    }
                }
                if (arrayOk) {
                    portOk = true;
                }
            } else {
                portOk = true;
            }
        }

        return portNum;
    }

}
