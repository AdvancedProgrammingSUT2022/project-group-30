package controllers;

import netPackets.Request;
import netPackets.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkController {
    private final static int PORT_NUMBER = 6000;
    private static NetworkController networkController;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private NetworkController(){

    }

    public static NetworkController getNetworkController(){
        if (networkController == null) {
            networkController = new NetworkController();
        }
        return networkController;
    }

    public void initializeNetwork(){
        try {
            socket = new Socket("localhost", PORT_NUMBER);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Response transferData(Request request){
        try {
            dataOutputStream.writeUTF(request.toJson());
            dataOutputStream.flush();
            return Response.formJson(dataInputStream.readUTF());
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void terminateNetwork(){
        try {
            socket.close();
            dataInputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
