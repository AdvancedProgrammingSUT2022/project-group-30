package controllers;

import com.google.gson.Gson;
import models.*;
import models.buildings.Building;
import models.diplomacy.Diplomacy;
import models.diplomacy.Message;
import models.improvements.Improvement;
import models.technology.TechnologyMap;
import models.units.Unit;
import models.works.Work;
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
                Class argumentClass = method.getParameterTypes()[i];
                if (argumentClass == Unit.class) {
                    int id = ((Unit) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameDataBase.getGameDataBase().findUnitById(id);
                } else if (argumentClass == City.class) {
                    int id = ((City) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameDataBase.getGameDataBase().findCityById(id);
                } else if (argumentClass == Civilization.class) {
                    int id = ((Civilization) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameDataBase.getGameDataBase().findCivById(id);
                } else if (argumentClass == Tile.class) {
                    int id = ((Tile) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameMap.getGameMap().findTileById(id);
                } else if (argumentClass == Ruins.class) {
                    int id = ((Ruins) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameDataBase.getGameDataBase().findRuinsById(id);
                } else if (argumentClass == User.class) {
                    int id = ((User) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = ProgramDatabase.getProgramDatabase().getUserById(id);
                } else if (argumentClass == Improvement.class) {
                    int id = ((Improvement) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findImprovementById(id);
                } else if (argumentClass == Building.class) {
                    int id = ((Building) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findBuildingById(id);
                } else if (argumentClass == Diplomacy.class) {
                    int id = ((Diplomacy) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findDiplomacyById(id);
                } else if (argumentClass == Message.class) {
                    int id = ((Message) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findMessageById(id);
                } else if (argumentClass == TechnologyMap.class) {
                    int id = ((TechnologyMap) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findTechnologyMapById(id);
                } else if (argumentClass == Work.class) {
                    int id = ((Work) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findWorkById(id);
                } else if (argumentClass == Citizen.class) {
                    int id = ((Citizen) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findCitizenById(id);
                }  else if (argumentClass == Notification.class) {
                    int id = ((Notification) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findNotificationById(id);
                } else {
                    parsedArguments[i] = gson.fromJson(arguments.get(i), argumentClass);
                }
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
