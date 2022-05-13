package models;

import models.interfaces.TileImage;
import models.resources.BonusResource;
import models.resources.LuxuryResource;
import models.resources.Resource;
import models.resources.StrategicResource;
import utilities.Debugger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class GameMap {
    private static GameMap gameMap;

    public static final int EXPENSIVE_MOVEMENT_COST = 10000;

    private Tile[][] map;
    private ArrayList<RiverSegment> rivers = new ArrayList<>();

    private GameMap() {

    }

    public void loadMapFromFile() {
        this.initializeMap();
        this.initializeRivers();
        this.initializeFeatures();
        this.initializeBonusResources();
        this.initializeLuxuryResources();
        this.initializeStrategicResources();
    }

    public static GameMap getGameMap() {
        if (gameMap != null) {
            return gameMap;
        }
        gameMap = new GameMap();
        return gameMap;
    }

    private void initializeMap() {
        File main = new File("src", "main");
        File java = new File(main, "java");
        File resources = new File(java, "resources");
        File mapFile = new File(resources, "map1.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(mapFile);
            ArrayList<String> fileLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                fileLines.add(input);
            }
            String mapTerrainTypes[][] = new String[fileLines.size()][];
            for (int i = 0; i < fileLines.size(); i++) {
                String tokens[] = fileLines.get(i).split("\\s+");
                mapTerrainTypes[i] = tokens;
            }
            this.map = new Tile[mapTerrainTypes.length][mapTerrainTypes[0].length];
            for (int i = 0; i < this.map.length; i++) {
                for (int j = 0; j < this.map[i].length; j++) {
                    map[i][j] = new Tile(this.findTileTerrainTypeFromFile(mapTerrainTypes[i][j]), new HashMap<>(), null);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initializeRivers() {
        File main = new File("src", "main");
        File java = new File(main, "java");
        File resources = new File(java, "resources");
        File riversFile = new File(resources, "map1Rivers.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(riversFile);
            ArrayList<String> fileLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                fileLines.add(scanner.nextLine());
            }
            for (int i = 0; i < fileLines.size(); i++) {
                String tokens[] = fileLines.get(i).split("\\s+");
                for (int j = 0; j < tokens.length; j++) {
                    String coordinates[] = tokens[j].split("-");
                    int firstTileYCoordinate = Integer.parseInt(coordinates[0]);
                    int firstTileXCoordinate = Integer.parseInt(coordinates[1]);
                    int secondTileYCoordinate = Integer.parseInt(coordinates[2]);
                    int secondTileXCoordinate = Integer.parseInt(coordinates[3]);
                    if (RiverSegment.checkTilesCoordinatesValidity(firstTileXCoordinate, firstTileYCoordinate,
                            secondTileXCoordinate, secondTileYCoordinate)) {
                        Tile firstTile = this.map[firstTileYCoordinate][firstTileXCoordinate];
                        Tile secondTile = this.map[secondTileYCoordinate][secondTileXCoordinate];
                        RiverSegment river = new RiverSegment(firstTile, secondTile);
                        this.rivers.add(river);
                    }
                }

            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initializeStrategicResources() {
        Random rand = new Random();
        ArrayList<Tile> compatiblTiles = new ArrayList<>();
        compatiblTiles = this.findAllCompatibleTilesForAResource(StrategicResource.COAL);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(StrategicResource.COAL, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(StrategicResource.HORSE);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(StrategicResource.HORSE, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(StrategicResource.IRON);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(StrategicResource.IRON, 1);
            }
        }

    }

    private void initializeLuxuryResources() {
        Random rand = new Random();
        ArrayList<Tile> compatiblTiles = new ArrayList<>();
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.COTTON);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.COTTON, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.DYE);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.DYE, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.FUR);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.FUR, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.GEM);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.GEM, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.GOLD);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.GOLD, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.INCENSE);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.INCENSE, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.IVORY);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.IVORY, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.MARBLE);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.MARBLE, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.SILK);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.SILK, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.SILVER);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.SILVER, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(LuxuryResource.SUGAR);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3) {
                compatiblTiles.get(i).getResources().put(LuxuryResource.SUGAR, 1);
            }
        }
    }

    private void initializeBonusResources() {
        Random rand = new Random();
        ArrayList<Tile> compatiblTiles = new ArrayList<>();
        compatiblTiles = this.findAllCompatibleTilesForAResource(BonusResource.BANANA);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 4) {
                compatiblTiles.get(i).getResources().put(BonusResource.BANANA, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(BonusResource.COW);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 4) {
                compatiblTiles.get(i).getResources().put(BonusResource.COW, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(BonusResource.GAZELLE);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 4) {
                compatiblTiles.get(i).getResources().put(BonusResource.GAZELLE, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(BonusResource.SHEEP);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 4) {
                compatiblTiles.get(i).getResources().put(BonusResource.SHEEP, 1);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAResource(BonusResource.WHEAT);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 4) {
                compatiblTiles.get(i).getResources().put(BonusResource.WHEAT, 1);
            }
        }
    }

    private void initializeFeatures() {
        Random rand = new Random();
        ArrayList<Tile> compatiblTiles = new ArrayList<>();
        compatiblTiles = this.findAllCompatibleTilesForAFeature(Feature.FLOOD_PLAINS);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 8 && compatiblTiles.get(i).getFeatures().size() < 3) {
                compatiblTiles.get(i).addFeatureAndApplyChanges(Feature.FLOOD_PLAINS);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAFeature(Feature.FOREST);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 4 && (compatiblTiles.get(i).getTerrainType() == TerrainType.GRASSLAND || compatiblTiles.get(i).getTerrainType() == TerrainType.MOUNTAIN || compatiblTiles.get(i).getTerrainType() == TerrainType.PLAINS) && compatiblTiles.get(i).getFeatures().size() < 3) {
                compatiblTiles.get(i).addFeatureAndApplyChanges(Feature.FOREST);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAFeature(Feature.ICE);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 3 && (compatiblTiles.get(i).getTerrainType() == TerrainType.SNOW || compatiblTiles.get(i).getTerrainType() == TerrainType.TUNDRA) && compatiblTiles.get(i).getFeatures().size() < 3) {
                compatiblTiles.get(i).addFeatureAndApplyChanges(Feature.ICE);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAFeature(Feature.JUNGLE);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 4 && (compatiblTiles.get(i).getTerrainType() == TerrainType.GRASSLAND || compatiblTiles.get(i).getTerrainType() == TerrainType.MOUNTAIN || compatiblTiles.get(i).getTerrainType() == TerrainType.PLAINS) && compatiblTiles.get(i).getFeatures().size() < 3) {
                compatiblTiles.get(i).addFeatureAndApplyChanges(Feature.JUNGLE);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAFeature(Feature.MARSH);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 4 && (compatiblTiles.get(i).getTerrainType() == TerrainType.DESERT || compatiblTiles.get(i).getTerrainType() == TerrainType.PLAINS) && compatiblTiles.get(i).getFeatures().size() < 3) {
                compatiblTiles.get(i).addFeatureAndApplyChanges(Feature.MARSH);
            }
        }
        compatiblTiles = this.findAllCompatibleTilesForAFeature(Feature.OASIS);
        for (int i = 0; i < compatiblTiles.size(); i++) {
            int chance = rand.nextInt(10);
            if (chance < 4 && compatiblTiles.get(i).getTerrainType() == TerrainType.DESERT && compatiblTiles.get(i).getFeatures().size() < 3) {
                compatiblTiles.get(i).addFeatureAndApplyChanges(Feature.OASIS);
            }
        }
    }

    private TerrainType findTileTerrainTypeFromFile(String terrainType) {
        if (terrainType.equals("Snow")) {
            return TerrainType.SNOW;
        } else if (terrainType.equals("Ocean")) {
            return TerrainType.OCEAN;
        } else if (terrainType.equals("Plains")) {
            return TerrainType.PLAINS;
        } else if (terrainType.equals("Grassland")) {
            return TerrainType.GRASSLAND;
        } else if (terrainType.equals("Mountain")) {
            return TerrainType.MOUNTAIN;
        } else if (terrainType.equals("Tundra")) {
            return TerrainType.TUNDRA;
        } else if (terrainType.equals("Hills")) {
            return TerrainType.HILLS;
        } else if (terrainType.equals("Desert")) {
            return TerrainType.DESERT;
        }
        return null;
    }

    public Tile[][] findTilesToPrint(Civilization civ) {
        Tile tiles[][] = new Tile[3][6];
        int startingXPoint = civ.getFrameBase().findTileXCoordinateInMap();
        int startingYPoint = civ.getFrameBase().findTileYCoordinateInMap();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = this.map[i + startingYPoint][j + startingXPoint];
            }
        }
        return tiles;
    }

    public TileImage[][] getCivilizationsImage(Civilization civ) {
        TileImage tiles[][] = new TileImage[3][6];
        int startingXPoint = civ.getFrameBase().findTileXCoordinateInMap();
        int startingYPoint = civ.getFrameBase().findTileYCoordinateInMap();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = civ.getTileImage(this.map[i + startingYPoint][j + startingXPoint]);
            }
        }
        return tiles;
    }

    public ArrayList<RiverSegment> findTilesRiverSegments(Tile tile) {
        ArrayList<RiverSegment> riverSegments = new ArrayList<>();
        for (int i = 0; i < this.rivers.size(); i++) {
            if (rivers.get(i).getFirstTile() == tile || rivers.get(i).getSecondTile() == tile) {
                riverSegments.add(rivers.get(i));
            }
        }
        return riverSegments;
    }

    public Tile getTile(int x, int y) {
        if (y >= map.length || y < 0 || x >= map[y].length || x < 0) {
            Debugger.debug("getTile of GameMap called with invalid indices x:" + x + ", y: " + y);
            return null;
        }
        return map[y][x];
    }

    public boolean areCoordinatesValid(int x, int y) {
        if (y >= map.length || y < 0 || x >= map[y].length || x < 0) {
            return false;
        }
        return true;
    }

    public Tile[][] getMap() {

        return this.map;
    }

    public ArrayList<RiverSegment> getRivers() {
        return this.rivers;
    }

    private ArrayList<Tile> findAllCompatibleTilesForAFeature(Feature feature) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                if (Feature.isTileCompatibleWithFeature(feature, this.map[i][j])) {
                    tiles.add(this.map[i][j]);
                }
            }
        }
        return tiles;
    }

    private ArrayList<Tile> findAllCompatibleTilesForAResource(Resource resource) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                if (resource.isTileValid(this.map[i][j])) {
                    tiles.add(this.map[i][j]);
                }
            }
        }
        return tiles;
    }

    public ArrayList<Tile> getAllMapTiles() {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                tiles.add(map[i][j]);
            }
        }
        return tiles;
    }
}