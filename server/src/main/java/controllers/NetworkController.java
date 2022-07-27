package controllers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonToken;
import models.*;
import models.buildings.Building;
import models.diplomacy.Diplomacy;
import models.diplomacy.DiplomaticRelation;
import models.diplomacy.DiplomaticMessage;
import models.improvements.Improvement;
import models.interfaces.Selectable;
import models.interfaces.TileImage;
import models.interfaces.combative;
import models.resources.BonusResource;
import models.resources.LuxuryResource;
import models.resources.Resource;
import models.resources.StrategicResource;
import models.technology.TechnologyMap;
import models.units.Unit;
import models.works.Work;
import netPackets.Request;
import netPackets.Response;
import utilities.MyGson;

import java.io.*;
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

    private int nextToken = 0;
    public synchronized int getToken() {
        int tokenCopy = nextToken;
        nextToken++;
        return tokenCopy;
    }

    private String process(String data) {
//        System.out.println("processing: " + data);
        Request request = Request.fromJson(data);
        try {
            Method method = null/*= GameController.class.getMethod(request.getMethodName())*/;
            Method[] methods = null;
            Object controller = null;
            if (request.getControllerName().equals("GameController")) {
                methods = GameController.getGameController().getClass().getDeclaredMethods();
                controller = GameController.getGameController();
            } else if (request.getControllerName().equals("ChatController")) {
                methods = ChatController.getChatController().getClass().getDeclaredMethods();
                controller = ChatController.getChatController();
            } else if (request.getControllerName().equals("ProgramController")) {
                methods = ProgramController.getProgramController().getClass().getDeclaredMethods();
                controller = ProgramController.getProgramController();
            }
//            System.out.println("Processing: " + request.getMethodName());
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(request.getMethodName()) && methods[i].getParameterCount() == request.getArguments().size()) {
                    method = methods[i];
                    break;
                }
            }
            ArrayList<String> arguments = request.getArguments();
            Object[] parsedArguments = new Object[arguments.size()];
            for (int i = 0; i < arguments.size(); i++) {
                Gson gson = MyGson.getGson();
                Class argumentClass = method.getParameterTypes()[i];
                if(argumentClass == combative.class){
                    System.out.println("its combative");
                    combative com = (combative) gson.fromJson(arguments.get(i), argumentClass);
                    if(com instanceof Unit){
                        int id = ((Unit) gson.fromJson(arguments.get(i), Unit.class)).getId();
                        parsedArguments[i] = GameDataBase.getGameDataBase().findUnitById(id);
                    } else{
                        int id = ((City) gson.fromJson(arguments.get(i), City.class)).getId();
                        parsedArguments[i] = GameDataBase.getGameDataBase().findCityById(id);
                    }
                } else if(argumentClass == Selectable.class){
                    System.out.println("its combative");
                    Selectable sel = (Selectable) gson.fromJson(arguments.get(i), argumentClass);
                    if(sel instanceof Unit){
                        if(((Unit) gson.fromJson(arguments.get(i), Unit.class)) != null) {
                            int id = ((Unit) gson.fromJson(arguments.get(i), Unit.class)).getId();
                            parsedArguments[i] = GameDataBase.getGameDataBase().findUnitById(id);
                        }
                    }
                    else {
                        if(((City) gson.fromJson(arguments.get(i), City.class)) != null) {
                            int id = ((City) gson.fromJson(arguments.get(i), City.class)).getId();
                            parsedArguments[i] = GameDataBase.getGameDataBase().findCityById(id);
                        }
                    }
                } else if (argumentClass == Unit.class) {
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
                } else if (argumentClass == LuxuryResource.class) {
                    String name = ((LuxuryResource) gson.fromJson(arguments.get(i), argumentClass)).getName();
                    parsedArguments[i] = LuxuryResource.getLuxuryResourceByName(name);
                } else if (argumentClass == StrategicResource.class) {
                    String name = ((StrategicResource) gson.fromJson(arguments.get(i), argumentClass)).getName();
                    parsedArguments[i] = StrategicResource.getStrategicResourceByName(name);
                } else if (argumentClass == BonusResource.class) {
                    String name = ((BonusResource) gson.fromJson(arguments.get(i), argumentClass)).getName();
                    parsedArguments[i] = BonusResource.getBonusResourceByName(name);
                } else {
                    parsedArguments[i] = gson.fromJson(arguments.get(i), argumentClass);
                }
            }
            Object result = method.invoke(controller, (Object[]) parsedArguments);
            if (result == null) {
                return MyGson.toJson(new Response(MyGson.toJson(null)));
            } else {
                Gson gson = MyGson.getGson();
                Response response;
                if (result instanceof Unit) {
//                    Unit castedResult = (Unit) result;
//                    HashMap<Tile, TileImage> map = castedResult.getOwner().getMapImage();
//                    castedResult.getOwner().setMapImage(null);
//                    response = new Response(gson.toJson(result));
//                    castedResult.getOwner().setMapImage(map);
                    Unit castedResult = (Unit) result;
                    response = new Response(gson.toJson(new Unit(castedResult)));
                } else if (result instanceof City) {
//                    City castedResult = (City) result;
//                    HashMap<Tile, TileImage> map = castedResult.getOwner().getMapImage();
//                    castedResult.getOwner().setMapImage(null);
//                    response = new Response(gson.toJson(result));
//                    castedResult.getOwner().setMapImage(map);

                    City castedResult = (City) result;
                    response = new Response(gson.toJson(new City(castedResult)));

                } else if (result instanceof Civilization) {
//                    Civilization castedResult = (Civilization) result;
//                    HashMap<Tile, TileImage> map = castedResult.getMapImage();
//                    castedResult.setMapImage(null);
//                    response = new Response(gson.toJson(result));
//                    castedResult.setMapImage(map);
                    Civilization castedResult = (Civilization) result;
                    response = new Response(gson.toJson(new Civilization(castedResult)));
                } else if (result instanceof DiplomaticRelation) {
                    DiplomaticRelation castedResult = (DiplomaticRelation) result;
                    response = new Response(gson.toJson(new DiplomaticRelation(castedResult)));
                } else if (result instanceof ArrayList<?> && ((ArrayList) result).size() > 0 && ((ArrayList) result).get(0) instanceof Unit) {
                    ArrayList<Unit> castedResult = (ArrayList<Unit>) result;
                    for (int i = castedResult.size() - 1; i >= 0; i--) {
                        castedResult.set(i, new Unit(castedResult.get(i)));
                    }
                    response = new Response(gson.toJson(castedResult));
                } else if (result instanceof ArrayList<?> && ((ArrayList) result).size() > 0 && ((ArrayList) result).get(0) instanceof Civilization) {
                    ArrayList<Civilization> castedResult = (ArrayList<Civilization>) result;
                    for (int i = castedResult.size() - 1; i >= 0; i--) {
                        castedResult.set(i, new Civilization(castedResult.get(i)));
                    }
                    response = new Response(gson.toJson(castedResult));
                } else if (result instanceof ArrayList<?> && ((ArrayList) result).size() > 0 && ((ArrayList) result).get(0) instanceof DiplomaticRelation) {
                    ArrayList<DiplomaticRelation> castedResult = (ArrayList<DiplomaticRelation>) result;
                    for (int i = castedResult.size() - 1; i >= 0; i--) {
                        castedResult.set(i, new DiplomaticRelation(castedResult.get(i)));
                    }
                    response = new Response(gson.toJson(castedResult));
                } else if (result instanceof ArrayList<?> && ((ArrayList) result).size() > 0 && ((ArrayList) result).get(0) instanceof City) {
                    ArrayList<City> castedResult = (ArrayList<City>) result;
                    for (int i = castedResult.size() - 1; i >= 0; i--) {
                        castedResult.set(i, new City(castedResult.get(i)));
                    }
                    response = new Response(gson.toJson(castedResult));
                } else {
                    response = new Response(gson.toJson(result));
                }
                return response.toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (request.getMethodName().equals("findTileXCoordinateInMap")) {
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
                        int connectionToken = getToken();
                        dataOutputStream.writeInt(connectionToken);
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
