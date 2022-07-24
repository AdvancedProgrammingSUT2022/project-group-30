package controllers;

import com.google.gson.Gson;
import models.*;
import models.buildings.Building;
import models.diplomacy.Diplomacy;
import models.diplomacy.Message;
import models.improvements.Improvement;
import models.interfaces.TileImage;
import models.technology.TechnologyMap;
import models.units.Unit;
import models.works.Work;
import netPackets.Request;
import netPackets.Response;
import utilities.MyGson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NetworkController {

    private final static int PORT_NUMBER = 6000;

    private static NetworkController networkController;

    private ServerSocket serverSocket;

    private NetworkController() {
    }

    public static NetworkController getNetworkController() {
        if (networkController == null) {
            networkController = new NetworkController();
        }
        return networkController;
    }

    private String process(String data) {
//        System.out.println(data);
        Request request = Request.fromJson(data);
        try {
            Method method = null/*= GameController.class.getMethod(request.getMethodName())*/;
            Method[] methods = GameController.getGameController().getClass().getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(request.getMethodName())) {
                    method = methods[i];
                    break;
                }
            }
            ArrayList<String> arguments = request.getArguments();
            Object[] parsedArguments = new Object[arguments.size()];
            for (int i = 0; i < arguments.size(); i++) {
                Gson gson = MyGson.getGson();
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
                } else if (argumentClass == Notification.class) {
                    int id = ((Notification) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findNotificationById(id);
                } else if (argumentClass == Player.class) {
                    int id = ((Player) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findPlayerById(id);
                } else if (argumentClass == RiverSegment.class) {
                    int id = ((RiverSegment) gson.fromJson(arguments.get(i), argumentClass)).getId();
                    parsedArguments[i] = GameController.getGameController().findRiverSegmentById(id);
                } else {
                    parsedArguments[i] = gson.fromJson(arguments.get(i), argumentClass);
                }
            }
            System.out.println("Processing Req for: " + method.getName());
            Object result = method.invoke(GameController.getGameController(), (Object[]) parsedArguments);
            if (result == null) {
                return MyGson.toJson(new Response(MyGson.toJson(null)));
            } else {
                Gson gson = MyGson.getGson();
                Response response;
                if (result instanceof Unit) {
//                    Unit castedResult = (Unit) result;
//                    HashMap<Tile, TileImage> map = castedResult.getOwner().getMapImage();
//                    castedResult.getOwner().setMapImage(null);
//                    response = new Response(gson.toJson(castedResult));
//                    castedResult.getOwner().setMapImage(map);
                    response = new Response(gson.toJson(new Unit((Unit) result)));
                } else if (result instanceof City) {
//                    City castedResult = (City) result;
////                    HashMap<Tile, TileImage> map = castedResult.getOwner().getMapImage();
//                    Civilization mainOwner = castedResult.getOwner();
//                    Civilization founder = castedResult.getFounder();
//                    castedResult.setOwner(new Civilization(mainOwner));
//                    castedResult.setFounder(new Civilization(founder));
//
//                    System.out.println("returning city: " + castedResult);
//                    System.out.println("returning city: " + new Gson().toJson(castedResult));
////                    castedResult.getOwner().setMapImage(null);
//                    response = new Response(gson.toJson(castedResult));
////                    castedResult.getOwner().setMapImage(map);
//                    castedResult.setOwner(mainOwner);
//                    castedResult.setFounder(founder);
                    City castedResult = (City) result;
                    response = new Response(gson.toJson(new City(castedResult)));
                } else if (result instanceof Civilization) {
                    Civilization castedResult = (Civilization) result;
                    HashMap<Tile, TileImage> map = castedResult.getMapImage();
                    City capital = castedResult.getCapital();
                    City originalCapital = castedResult.getOriginCapital();
                    castedResult.setMapImage(null);
                    castedResult.setCapital(null);
                    castedResult.setOriginCapital(null);
                    response = new Response(gson.toJson(castedResult));
                    castedResult.setMapImage(map);
                    castedResult.setOriginCapital(originalCapital);
                    castedResult.setCapital(capital);
                } else if (result instanceof ArrayList<?> && ((ArrayList) result).size() > 0 && ((ArrayList) result).get(0) instanceof Unit) {
//                    ArrayList<Unit> castedResult = (ArrayList<Unit>) result;
//                    ArrayList<HashMap<Tile, TileImage>> maps = new ArrayList<>();
//                    for (int i = 0; i < castedResult.size(); i++) {
//                        maps.add(castedResult.get(i).getOwner().getMapImage());
//                        castedResult.get(i).getOwner().setMapImage(null);
//                    }
//                    response = new Response(gson.toJson(result));
//                    for (int i = 0; i < castedResult.size(); i++) {
//                        castedResult.get(i).getOwner().setMapImage(maps.get(i));
//                    }
                    ArrayList<Unit> castedResult = (ArrayList<Unit>) result;
                    ArrayList<Unit> processedResult = new ArrayList<>();
                    for (Unit unit : castedResult) {
                        processedResult.add(new Unit(unit));
                    }
                    response = new Response(gson.toJson(processedResult));
                } else {
                    response = new Response(gson.toJson(result));
                }
                return response.toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(request.getMethodName().equals("findTileXCoordinateInMap")){
            System.out.println("WTF IS HAPPENING!!!!");
        }
        return null;
    }

    public void run() {
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
                                int length = dataInputStream.readInt();
                                byte[] data = new byte[length];
                                dataInputStream.readFully(data);
                                String request = new String(data);
                                String response = process(request);
                                data = response.getBytes(StandardCharsets.UTF_8);
                                dataOutputStream.writeInt(data.length);
                                dataOutputStream.write(data);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
