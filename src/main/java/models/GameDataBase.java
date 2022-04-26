package models;

import java.util.ArrayList;

import models.diplomacy.Diplomacy;
import models.diplomacy.WarInfo;
import models.units.Unit;

public class GameDataBase {
    private static GameDataBase gameDataBase = null;
    private ArrayList<Civilization> civilizations = new ArrayList<>();
    private GameMap map = GameMap.getGameMap();
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<WarInfo> wars = new ArrayList<>();
    private ArrayList<CivilizationPair> civilizationPairs = new ArrayList<>();
    private Civilization currentPlayer;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Diplomacy> AllDiplomaticRelations = new ArrayList<>();

    private GameDataBase() {
        map = GameMap.getGameMap();
    }

    public static GameDataBase getGameDataBase() {
        if (gameDataBase == null)
            gameDataBase = new GameDataBase();
        return gameDataBase;
    }

    public ArrayList<Civilization> getCivilizations() {
        return this.civilizations;
    }

    public GameMap getMap() {
        return this.map;
    }

    public ArrayList<City> getCities() {
        return this.cities;
    }

    public ArrayList<Unit> getUnits() {
        return this.units;
    }

    public ArrayList<WarInfo> getWars() {
        return this.wars;
    }

    public void addWar(WarInfo war) {
        // TODO .. add other effects
        // let me know about best practice -> should I have if?? what about else and
        // debugger??
        if (!this.wars.contains(war))
            this.wars.add(war);
    }

    public ArrayList<CivilizationPair> getCivilizationPairs() {
        return this.civilizationPairs;
    }

    public ArrayList<Diplomacy> getAllDiplomaticRelations() {
        return this.AllDiplomaticRelations;
    }

    public Civilization getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Civilization currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayers(User[] players) {
        for (User player : players) {
            if (player != null) {
                this.players.add(new Player(player));
            }
        }
    }
}
