package controllers;

import netPackets.Request;
import netPackets.Response;
import utilities.MyGson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;

public class NetworkController {
    private final static int PORT_NUMBER = 6000;
    private static NetworkController networkController;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private NetworkController() {

    }

    public static NetworkController getNetworkController() {
        if (networkController == null) {
            networkController = new NetworkController();
        }
        return networkController;
    }

    public void initializeNetwork() {
        try {
            socket = new Socket("localhost", PORT_NUMBER);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Object transferData(Request request) {
        try {
            dataOutputStream.writeUTF(request.toJson());
            dataOutputStream.flush();
            Method method = null;
            Method[] methods = GameController.getGameController().getClass().getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(request.getMethodName())) {
                    method = methods[i];
                    break;
                }
            }
            if (method.getReturnType() == void.class) {
                return null;
            }

            Response response = Response.fromJson(dataInputStream.readUTF());
            return MyGson.getGson().fromJson(response.getJson(), method.getReturnType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void terminateNetwork() {
        try {
            socket.close();
            dataInputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
