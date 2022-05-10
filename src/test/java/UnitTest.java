import controllers.GameController;
import models.*;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
import models.buildings.BuildingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class UnitTest {
    @Mock
    Tile tile1;

    @Mock
    Tile tile2;

    @Test
    public void test(){
        when(tile1.findTileXCoordinateInMap()).thenReturn(1);
        when(tile1.findTileYCoordinateInMap()).thenReturn(1);
        when(tile2.findTileXCoordinateInMap()).thenReturn(1);
        when(tile2.findTileYCoordinateInMap()).thenReturn(2);
        Assertions.assertTrue(GameController.getGameController().areTwoTilesAdjacent(tile1, tile2));
    }

    @Mock
    Tile tile;

    @Mock
    City city;

    @Test
    public void test3(){
        when(city.hasBuildingType(BuildingType.GRANARY)).thenReturn(true);
        when(city.hasBuildingType(BuildingType.WATER_MILL)).thenReturn(true);
        when(city.hasBuildingType(BuildingType.MINT)).thenReturn(false);
        when(city.hasBuildingType(BuildingType.PALACE)).thenReturn(true);
        when(city.calculateOutputOfBuildings()).thenCallRealMethod();
        Output output = city.calculateOutputOfBuildings();
        Output expectedOutput = new Output(2, 4, 2);
        Assertions.assertEquals(output, expectedOutput);
    }

    @BeforeEach
    public void test5(){
        GameMap.getGameMap().loadMapFromFile();
    }


    @Test
    public void test4(){
        when(tile.findTileXCoordinateInMap()).thenReturn(10);
        when(tile.findTileYCoordinateInMap()).thenReturn(10);
        when(tile.getTerrainType()).thenReturn(TerrainType.PLAINS);
        ArrayList<Tile> tiles = GameController.getGameController().getVisibleTilesFromTile(tile, 1);
        Assertions.assertEquals(6, tiles.size());
        tiles = GameController.getGameController().getVisibleTilesFromTile(tile, 0);
        Assertions.assertNull(tiles);
    }

/*    public ArrayList<Tile> getVisibleTilesByCities(Civilization civilization) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (City city : GameDataBase.getGameDataBase().getCities()) {
            if (!city.getOwner().equals(civilization)) continue;
            for (Tile tile : city.getTerritories()) {
                tiles.addAll(getVisibleTilesFromTile(tile, 1));
            }
            tiles.add(city.getCentralTile());
        }
        return deleteRepetitiveElementsFromArrayList(tiles);
    }*/

    @Mock
    City city1;

    @Mock
    City city2;

    @Mock
    Civilization civilization;


    @Test
    public void test6(){
        GameDataBase.getGameDataBase().getCities().add(city1);
        GameDataBase.getGameDataBase().getCities().add(city2);
        when(city1.getOwner()).thenReturn(civilization);
        when(city2.getOwner()).thenReturn(civilization);
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(tile1);
        tiles.add(tile2);
        when(tile1.findTileXCoordinateInMap()).thenReturn(10);
        when(tile1.findTileYCoordinateInMap()).thenReturn(10);
        when(tile1.getTerrainType()).thenReturn(TerrainType.PLAINS);
        when(tile2.findTileXCoordinateInMap()).thenReturn(20);
        when(tile2.findTileYCoordinateInMap()).thenReturn(20);
        when(tile2.getTerrainType()).thenReturn(TerrainType.GRASSLAND);
        when(city1.getTerritories()).thenReturn(tiles);
        when(city2.getTerritories()).thenReturn(tiles);
        ArrayList<Tile> expectedTiles = GameController.getGameController().getVisibleTilesByCities(civilization);
        Assertions.assertEquals(13, expectedTiles.size());
    }



}
