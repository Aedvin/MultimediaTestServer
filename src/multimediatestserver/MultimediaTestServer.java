/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multimediatestserver;

import Log.Log;
import Threads.UDPThread;

import java.util.Scanner;

public class MultimediaTestServer {


    public static void main(String[] args) {
        Log.init("");
        UDPThread thread = new UDPThread();
        thread.start();
        boolean running = true;
        Scanner scn = new Scanner(System.in);
        while (running) {
            if (scn.hasNext()) {
                if (scn.next().equals("exit")) {
                    thread.close();
                    try {
                        thread.join();
                    } catch (Exception e) {
                        Log.writeException("Main", e);
                    }
                    running = false;
                }
            }
        }
        Log.close();
    }

}
