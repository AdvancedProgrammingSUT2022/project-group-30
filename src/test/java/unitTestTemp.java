import controllers.GameController;
import models.GameDataBase;
import models.Tile;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
import models.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class unitTestTemp {
/*    @Mock
    Tile tile;

    @Mock
    TerrainProperty property;

    @Mock
    TerrainType terrainType;

    @Mock
    HashMap<Resource, Integer> resources;

    @Mock
    Ruins ruins;

    @Test
    public void checkIsOfType(){
        Tile tile = new Tile(terrainType, resources, ruins);
        when(tile.getTerrainType().equals(property) ).thenReturn(true);
        when(tile.getFeatures().contains(property)).thenReturn(false);
        Assert.assertTrue(tile.isOfType(property));
    }*/

/*    @Mock
    City city;

    @Test
    public void checkGetter(){
        when(city.getIsPuppet()).thenReturn(true);
        Assert.assertTrue(city.unitTestCity());
    }*/

/*    public boolean areTwoTilesAdjacent(Tile tile1, Tile tile2) {
        int x = tile1.findTileXCoordinateInMap();
        int y = tile1.findTileYCoordinateInMap();
        int x2 = tile2.findTileXCoordinateInMap();
        int y2 = tile2.findTileYCoordinateInMap();
        if (Math.abs(x - x2) > 1 || Math.abs(y - y2) > 1) {
            return false;
        }
        if (x % 2 == 0) {
            if (y2 - y == 1 && x != x2) {
                return false;
            }
            return true;
        } else {
            if (y - y2 == 1 && x != x2) {
                return false;
            }
            return true;
        }
    }*/

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

/*    public ArrayList<Unit> getUnitsInTile(Tile tile) {
        ArrayList<Unit> units = new ArrayList<>();
        for (Unit unit : GameDataBase.getGameDataBase().getUnits()) {
            if (unit.getLocation() == tile) {
                units.add(unit);
            }
        }
        return units;
    }*/


    @Mock
    Tile tile;



    @Test
    public void test2(){
        ArrayList<Unit> units = Mockito.mock(ArrayList.class);
        when(GameDataBase.getGameDataBase().getUnits()).thenReturn(units);


    }
}
