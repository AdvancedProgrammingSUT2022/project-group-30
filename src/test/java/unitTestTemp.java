import models.City;
import models.Ruins;
import models.TerrainType;
import models.Tile;
import models.interfaces.TerrainProperty;
import models.resources.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

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

    @Mock
    City city;

    @Test
    public void checkGetter(){
        when(city.getIsPuppet()).thenReturn(true);
        Assert.assertTrue(city.unitTestCity());
    }
}
