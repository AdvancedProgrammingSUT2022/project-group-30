package controllers;

import java.net.Socket;

public class NetworkController {
    private final static int PORT_NUMBER = 6000;
    private static NetworkController networkController;

    private NetworkController() {

    }

    public static NetworkController getNetworkController() {
        if (networkController == null) {
            networkController = new NetworkController();
        }
        return networkController;
    }

    public void initializeNetwork() {
        //Socket socket = new Socket("localhost", PORT_NUMBER);
    }
}
