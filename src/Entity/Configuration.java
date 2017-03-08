/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

public class Configuration {

    private int mServerPort;
    private int mClientPort;
    private int mPacketSize;
    private int mPacketCount;
    private String mPacketAddr;

    public Configuration(int serverPort) {
        mServerPort = serverPort;
    }

    public void setClientPort(int clientPort) {
        mClientPort = clientPort;
    }

    public int getServerPort() {
        return mServerPort;
    }

    public int getClientPort() {
        return mClientPort;
    }
}
