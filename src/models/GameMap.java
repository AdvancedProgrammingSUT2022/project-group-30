package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameMap {
    private static GameMap gameMap;

    //public static final int NUMBER_OF_TILES = 100;
    private Tile[][] map;
    private ArrayList<RiverSegment> rivers;
    private Tile frameBase;

    private GameMap() {
        this.initializeMap();
        this.rivers = new ArrayList<>();
        this.initializeRivers();
        this.frameBase = null;
        //map = new Tile[NUMBER_OF_TILES][NUMBER_OF_TILES];
        //rivers = new ArrayList<>();
    }

    public static GameMap getGameMap(){
        if(gameMap != null){
            return gameMap;
        }
        gameMap = new GameMap();
        return gameMap;
    }

    private void initializeMap(){
        File mapFile = new File("utilities","map1.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(mapFile);
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
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void initializeRivers(){
        File riversFile = new File("utilities","map1Rivers.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(riversFile);
            ArrayList<String> fileLines = new ArrayList<>();
            while(scanner.hasNextLine()){
                fileLines.add(scanner.nextLine());
            }
            for(int i = 0; i < fileLines.size(); i++){
                String tokens[] = fileLines.get(i).split("\\s+");
                for(int j = 0; j < tokens.length; j++){
                    String coordinates[] = tokens[j].split("-");
                    int firstTileYCoordinate = Integer.parseInt(coordinates[0]);
                    int firstTileXCoordinate = Integer.parseInt(coordinates[1]);
                    int secondTileYCoordinate = Integer.parseInt(coordinates[2]);
                    int secondTileXCoordinate = Integer.parseInt(coordinates[3]);
                    if(RiverSegment.checkTilesCoordinatesValidity(firstTileXCoordinate, firstTileYCoordinate, secondTileXCoordinate, secondTileYCoordinate)){
                        Tile firstTile = this.map[firstTileYCoordinate][firstTileXCoordinate];
                        Tile secondTile = this.map[secondTileYCoordinate][secondTileXCoordinate];
                        RiverSegment river = new RiverSegment(firstTile, secondTile);
                        this.rivers.add(river);
                    }
                }

            }

            scanner.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
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

    public Tile[][] findTilesToPrint(){
        Tile tiles[][] = new Tile[3][6];
        int startingXPoint = this.frameBase.findTileXCoordinateInMap();
        int startingYPoint = this.frameBase.findTileYCoordinateInMap();
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                tiles[i][j] = this.map[i+startingYPoint][j+startingXPoint];
            }
        }
        return tiles;

    }

    public ArrayList<RiverSegment> findTilesRiverSegments(Tile tile){
        ArrayList<RiverSegment> riverSegments = new ArrayList<>();
        for(int i = 0; i < this.rivers.size(); i++){
            if(rivers.get(i).getFirstTile() == tile || rivers.get(i).getSecondTile() == tile){
                riverSegments.add(rivers.get(i));
            }
        }
        return riverSegments;
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

    public void setFrameBase(Tile tile){
        this.frameBase = tile;
    }

    public Tile getFrameBase(){
        return this.frameBase;
    }

}
