package controllers;

import com.google.gson.Gson;
import models.GameMap;
import models.ProgramDatabase;
import models.User;
import netPackets.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

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

    private String process(String data){
        Request request = Request.fromJson(data);
        try {
            Method method = null/*= GameController.class.getMethod(request.getMethodName())*/;
            Method[] methods = GameController.getGameController().getClass().getDeclaredMethods();
            for(int i = 0; i < methods.length; i++){
                if(methods[i].getName().equals(request.getMethodName())){
                    method = methods[i];
                    break;
                }
            }
            ArrayList<String> arguments = request.getArguments();
            Object[] parsedArguments = new Object[arguments.size()];
            for (int i = 0; i < arguments.size(); i++) {
                Gson gson = new Gson();
                /*
                * if... method.getParameterTypes()[i] Unit -> find the unit in server
                 */
                parsedArguments[i] = gson.fromJson(arguments.get(i), method.getParameterTypes()[i]);

            }
            Object result = method.invoke(GameController.getGameController(), (Object[]) parsedArguments);
            Gson gson = new Gson();
            return gson.toJson(result);

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void run(){
        User[] players = new User[2];
        LoginPageController.getLoginPageController().setProgramDatabase();
        players[0] = ProgramDatabase.getProgramDatabase().getUserByUsername("mahyarafshin");
        players[1] = ProgramDatabase.getProgramDatabase().getUserByUsername("amir");
        GameController.getGameController().addPlayers(players);
        GameController.getGameController().initializeGame(20, 30, 0, 8);
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
