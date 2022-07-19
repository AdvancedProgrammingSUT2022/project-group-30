package models;

import menusEnumerations.AutoSave;
import models.diplomacy.Diplomacy;
import models.diplomacy.DiplomaticRelation;
import models.diplomacy.WarInfo;
import models.units.Unit;

import java.util.ArrayList;

public class GameDataBase implements java.io.Serializable{
    private static GameDataBase gameDataBase = null;
    private GameMap map = GameMap.getGameMap();
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<WarInfo> wars = new ArrayList<>();
    private ArrayList<DiplomaticRelation> diplomaticRelations = new ArrayList<>();
    private Civilization currentPlayer;
    private ArrayList<Player> players = new ArrayList<>();

    private int turnNumber = 0;

    private int numberOfAutoSavedFiles = 5;

    private int currrentAutoSaveNumber = 1;

    private AutoSave autoSaveMode = AutoSave.OFF;

    private GameDataBase() {
        map = GameMap.getGameMap();
    }

    public static GameDataBase getGameDataBase() {
        if (gameDataBase == null)
            gameDataBase = new GameDataBase();
        return gameDataBase;
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
        if (!this.wars.contains(war))
            this.wars.add(war);
    }

    public ArrayList<CivilizationPair> getCivilizationPairs() {
        ArrayList<CivilizationPair> result = new ArrayList<>();
        for (DiplomaticRelation relation : diplomaticRelations) {
            result.add(relation.getPair());
        }
        return result;
    }

    public ArrayList<DiplomaticRelation> getDiplomaticRelations() {
        return this.diplomaticRelations;
    }

    public DiplomaticRelation getDiplomaticRelation(Civilization civ1, Civilization civ2) {
        if (civ1 == civ2) {
            return null;
        }
        for (DiplomaticRelation relation : diplomaticRelations) {
            if (relation.getPair().containsCivilization(civ1) && relation.getPair().containsCivilization(civ2)) {
                return relation;
            }
        }
        return null;
    }

    public ArrayList<Civilization> getDiscoveredCivilizations(Civilization reference) {
        ArrayList<Civilization> result = new ArrayList<>();
        for (Civilization civilization : getCivilizations()) {
            DiplomaticRelation relation = getDiplomaticRelation(civilization, reference);
            if (relation != null && relation.areMutuallyVisible()) {
                result.add(civilization);
            }
        }
        return result;
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

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public ArrayList<Civilization> getCivilizations() {
        ArrayList<Civilization> civilizations = new ArrayList<>();
        for (Player player : this.players) {
            if (player.getCivilization() != null) {
                civilizations.add(player.getCivilization());
            }
        }
        return civilizations;
    }

    public static void setGameDataBaseWithForce(GameDataBase gameDataBase){
        GameDataBase.gameDataBase = gameDataBase;
    }

    public AutoSave getAutoSaveMode() {
        return autoSaveMode;
    }

    public void setAutoSaveMode(AutoSave autoSaveMode) {
        this.autoSaveMode = autoSaveMode;
    }

    public int getNumberOfAutoSavedFiles() {
        return numberOfAutoSavedFiles;
    }

    public void setNumberOfAutoSavedFiles(int numberOfAutoSavedFiles) {
        this.numberOfAutoSavedFiles = numberOfAutoSavedFiles;
    }

    public int getCurrrentAutoSaveNumber() {
        return currrentAutoSaveNumber;
    }

    public void setCurrrentAutoSaveNumber(int currrentAutoSaveNumber) {
        this.currrentAutoSaveNumber = currrentAutoSaveNumber;
    }

    public ArrayList<String> getAllPlayersUsername(){
        ArrayList<String > names = new ArrayList<>();
        for (Player player : this.players) {
            names.add(player.getUser().getUsername());
        }
        return names;
    }
}
