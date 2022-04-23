package models;

import java.util.ArrayList;

import controllers.diplomacy.Diplomacy;
import models.interfaces.Selectable;
import models.units.Unit;

public class GameDataBase {
    private static GameDataBase gameDataBase = null;
    private Selectable selectedEntity;
    private ArrayList<Civilization> civilizations = new ArrayList<>();
    private GameMap map = GameMap.getGameMap();
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<WarInfo> wars = new ArrayList<>();
    private ArrayList<CivilizationPair> civilizationPairs= new ArrayList<>();
    private ArrayList<Diplomacy> diplomaticRelations = new ArrayList<>();

    private GameDataBase()  {
        map = GameMap.getGameMap();
    }
 
    public static GameDataBase getGameDataBase() {
        if (gameDataBase == null)
            gameDataBase = new GameDataBase();
        return gameDataBase;
    }

    public Selectable getSelectedEntity() {
        return this.selectedEntity;
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

  
    // public void initializeDiplomaticRelationMap(ArrayList<CivilizationPair> civilizationPairs) {
    //     for(CivilizationPair civilizationPair : civilizationPairs){
    //         //MINETODO .. create Diplomatic class and fill the following () with "civilizationPair, 0"
    //         this.diplomaticRelations.add(new DiplomaticRelation());
    //     }
    // }

    public ArrayList<CivilizationPair> getCivilizationPairs(){
        return this.civilizationPairs;
    }

    public ArrayList<Diplomacy> getDiplomaticRelations(){
        return this.diplomaticRelations;
    }
}
