package controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class NetworkController {

    private final static int PORT_NUMBER = 6000;

    private static NetworkController networkController;

    private ServerSocket serverSocket;

    private NetworkController(){

    }

    public static NetworkController getNetworkController(){
        if (networkController == null) {
            networkController = new NetworkController();
        }
        return networkController;
    }

    private String process(String request){
        return null;
    }

    public void run(){
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        while (true) {
                            try {
                                String request = dataInputStream.readUTF();
                                String response = process(request);
                                dataOutputStream.writeUTF(response);
                                dataOutputStream.flush();
                            } catch (SocketException | EOFException e) {
                                System.out.println("connection failed");
                                return;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
