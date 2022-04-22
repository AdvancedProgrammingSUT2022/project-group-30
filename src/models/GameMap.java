package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import views.PrintableCharacters;

public class GameMap {
    private static GameMap gameMap;

    //public static final int NUMBER_OF_TILES = 100;
    private Tile[][] map;
    private ArrayList<RiverSegment> rivers;
    private Tile frameBase;

    private GameMap() throws FileNotFoundException {
        this.initializeMap();
        this.rivers = new ArrayList<>();
        this.frameBase = null;
        //map = new Tile[NUMBER_OF_TILES][NUMBER_OF_TILES];
        //rivers = new ArrayList<>();
    }

    public static GameMap getGameMap() throws FileNotFoundException{
        if(gameMap != null){
            return gameMap;
        }
        gameMap = new GameMap();
        return gameMap;
    }

    private void initializeMap() throws FileNotFoundException{
        File mapFile = new File("../utilities/map1.txt");
        Scanner scanner = new Scanner(mapFile);
        ArrayList<String> fileLines = new ArrayList<>();
        while(scanner.hasNextLine()){
            String input = scanner.nextLine();
            fileLines.add(input);
        }
        String mapTerrainTypes[][] = new String[fileLines.size()][];
        for(int i = 0; i < fileLines.size(); i++){
            String tokens[] = fileLines.get(i).split("\\s+");
            mapTerrainTypes[i] = tokens;
        }
        this.map = new Tile[mapTerrainTypes.length][mapTerrainTypes[0].length];
        for(int i = 0; i < this.map.length; i++){
            for(int j = 0; j < this.map[i].length; j++){
                map[i][j] = new Tile(this.findTileTerrainTypeFromFile(mapTerrainTypes[i][j]), null, null, null, null);
            }
        }
        scanner.close();
    }

    private TerrainType findTileTerrainTypeFromFile(String terrainType){
        if(terrainType.equals("Snow")){
            return TerrainType.SNOW;
        }
        else if(terrainType.equals("Ocean")){
            return TerrainType.OCEAN;
        }
        else if(terrainType.equals("Plains")){
            return TerrainType.PLAINS;
        }
        else if(terrainType.equals("Grassland")){
            return TerrainType.GRASSLAND;
        }
        else if(terrainType.equals("Mountain")){
            return TerrainType.MOUNTAIN;
        }
        else if(terrainType.equals("Tundra")){
            return TerrainType.TUNDRA;
        }
        else if(terrainType.equals("Hills")){
            return TerrainType.HILLS;
        }
        else if(terrainType.equals("Desert")){
            return TerrainType.DESERT;
        }
        return null;
    }

    public Tile[][] findTilesToPrint() throws FileNotFoundException{
        Tile tiles[][] = new Tile[3][6];
        int startingXPoint = this.frameBase.findTileXCoordinateInMap();
        int startingYPoint = this.frameBase.findTileYCoordinateInMap();
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                tiles[i][j] = this.map[i+startingXPoint][j+startingYPoint];
            }
        }
        return tiles;
    }

    public ArrayList<Tile> findClosestPath(Tile origin, Tile destination){
        //TODO
        return null;
    }

    public int findMovementCost(Unit unit, Tile destination){
        //TODO
        return 0;
    }

    public Tile findTileToAppendToCity(City city){
        //TODO
        return null;
    }

    public ArrayList<Tile> getZOCTilesForCivilization(Civilization  civilization){
        //TODO
        return null;
    }

    public Tile[][] getMap(){
        
        return this.map;
    }

    public ArrayList<RiverSegment> getRivers (){
        return this.rivers;
    }

    public Tile getFrameBase(){
        return this.frameBase;
    }

}
