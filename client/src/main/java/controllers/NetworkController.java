package controllers;

import netPackets.Request;
import netPackets.Response;
import utilities.MyGson;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NetworkController {
    private final static int PORT_NUMBER = 6000;
    private static NetworkController networkController;

    private Socket socket;
    private int token;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private RuntimeException runtimeException;

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
            token = dataInputStream.readInt();
            System.out.println("token: " + token);
        } catch (IOException e) {
            throw runtimeException;
        }
    }

    public Object transferData(Request request) {
//        System.out.println("request: " + request.getMethodName());
        try {
            byte[] data = request.toJson().getBytes(StandardCharsets.UTF_8);
            dataOutputStream.writeInt(data.length);
            dataOutputStream.write(data);
            dataOutputStream.flush();
            Method method = null;
            Method[] methods = null;
            if (request.getControllerName().equals("GameController")) {
                methods = GameController.getGameController().getClass().getDeclaredMethods();
            } else if (request.getControllerName().equals("ChatController")) {
                methods = ChatController.getChatController().getClass().getDeclaredMethods();
            } else if (request.getControllerName().equals("ProgramController")) {
                methods = ProgramController.getProgramController().getClass().getDeclaredMethods();
            }
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(request.getMethodName())) {
                    method = methods[i];
                    break;
                }
            }
            int length = dataInputStream.readInt();
            data = new byte[length];
            dataInputStream.readFully(data);
            if (method.getReturnType() == void.class) {
                return null;
            }
            String text = new String(data);
//            System.out.println(text);
            Response response = Response.fromJson(text);
//            System.out.println("RESPONSE:\n" + response.getJson());
//            Files.writeString(Paths.get("json.txt"), response.getJson());
            return MyGson.getGson().fromJson(response.getJson(), method.getReturnType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> ArrayList<T> transferData(Request request, Class<T[]> clazz) {
        try {
            byte[] data = request.toJson().getBytes(StandardCharsets.UTF_8);
            dataOutputStream.writeInt(data.length);
            dataOutputStream.write(data);
            dataOutputStream.flush();
            Method method = null;
            Method[] methods = null;
            if (request.getControllerName().equals("GameController")) {
                methods = GameController.getGameController().getClass().getDeclaredMethods();
            } else if (request.getControllerName().equals("ChatController")) {
                methods = ChatController.getChatController().getClass().getDeclaredMethods();
            } else if (request.getControllerName().equals("ProgramController")) {
                methods = ProgramController.getProgramController().getClass().getDeclaredMethods();
            }
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(request.getMethodName())) {
                    method = methods[i];
                    break;
                }
            }
            if (method.getReturnType() == void.class) {
                return null;
            }
            int length = dataInputStream.readInt();
            data = new byte[length];
            dataInputStream.readFully(data);
            String text = new String(data);
            Response response = Response.fromJson(text);
            return new ArrayList<T>(Arrays.asList(MyGson.getGson().fromJson(response.getJson(), clazz)));
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

    public int getToken() {
        return token;
    }
}
