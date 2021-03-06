//import controllers.GameController;
//import models.*;
//import models.buildings.BuildingType;
//import models.diplomacy.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UnitTest {
//    @Mock
//    Tile tile;
//    @Mock
//    City city;
//    @Mock
//    Tile tile1;
//    @Mock
//    Tile tile2;
//    @Mock
//    City city1;
//    @Mock
//    City city2;
//    @Mock
//    Civilization civilization;
//
//    @Mock
//    Civilization civilization1;
//
//    @Mock
//    Civilization civilization2;
//
//    @Mock
//    CivilizationPair pair;
//
//    @Mock
//    Diplomacy diplomacy1;
//
//    @Mock
//    Diplomacy diplomacy2;
//
//    @BeforeEach
//    public void setup() {
//        GameMap.getGameMap().loadMapFromFile(52, 80, 0, 0);
//    }
//
//    @Test
//    public void testAreTwoTilesAdjacent() {
//        when(tile1.findTileXCoordinateInMap()).thenReturn(1);
//        when(tile1.findTileYCoordinateInMap()).thenReturn(1);
//        when(tile2.findTileXCoordinateInMap()).thenReturn(1);
//        when(tile2.findTileYCoordinateInMap()).thenReturn(2);
//        Assertions.assertTrue(GameController.getGameController().areTwoTilesAdjacent(tile1, tile2));
//    }
//
//    @Test
//    public void testCalculateOutputOfBuildings() {
//        when(city.hasBuildingType(BuildingType.GRANARY)).thenReturn(true);
//        when(city.hasBuildingType(BuildingType.WATER_MILL)).thenReturn(true);
//        when(city.hasBuildingType(BuildingType.MINT)).thenReturn(false);
//        when(city.hasBuildingType(BuildingType.PALACE)).thenReturn(true);
//        when(city.calculateOutputOfBuildings()).thenCallRealMethod();
//        Output output = city.calculateOutputOfBuildings();
//        Output expectedOutput = new Output(2, 4, 2);
//        Assertions.assertEquals(output, expectedOutput);
//    }
//
//    @Test
//    public void testGetVisibleTilesFromTile() {
//        when(tile.findTileXCoordinateInMap()).thenReturn(10);
//        when(tile.findTileYCoordinateInMap()).thenReturn(10);
//        when(tile.getTerrainType()).thenReturn(TerrainType.PLAINS);
//        ArrayList<Tile> tiles = GameController.getGameController().getVisibleTilesFromTile(tile, 1);
//        Assertions.assertEquals(6, tiles.size());
//        tiles = GameController.getGameController().getVisibleTilesFromTile(tile, 0);
//        Assertions.assertNull(tiles);
//    }
//
//    @Test
//    public void testGetDiplomaticRelationsMap() {
//        ArrayList<Civilization> civilizations = new ArrayList<>();
//        civilizations.add(civilization1);
//        civilizations.add(civilization2);
//        when(pair.getCivilizationsArray()).thenReturn(civilizations);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy1);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy2);
//        Diplomacy diplomacy = GameController.getGameController().getDiplomaticRelationsMap(pair);
//        Assertions.assertNull(diplomacy);
//    }
//
//    @Test
//    public void testGetScientificTreaties() {
//        ArrayList<Civilization> civilizations = new ArrayList<>();
//        civilizations.add(civilization1);
//        civilizations.add(civilization2);
//        when(pair.getCivilizationsArray()).thenReturn(civilizations);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy1);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy2);
//        ArrayList<ScientificTreaty> diplomacies = GameController.getGameController().getScientificTreaties(pair);
//        Assertions.assertEquals(0, diplomacies.size());
//    }
//
//    @Test
//    public void testGetStepWiseGoldTransferContracts() {
//        ArrayList<Civilization> civilizations = new ArrayList<>();
//        civilizations.add(civilization1);
//        civilizations.add(civilization2);
//        when(pair.getCivilizationsArray()).thenReturn(civilizations);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy1);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy2);
//        ArrayList<StepWiseGoldTransferContract> diplomacies = GameController.getGameController().getStepWiseGoldTransferContracts(pair);
//        Assertions.assertEquals(0, diplomacies.size());
//    }
//
//    @Test
//    public void testGetWarInfos() {
//        ArrayList<Civilization> civilizations = new ArrayList<>();
//        civilizations.add(civilization1);
//        civilizations.add(civilization2);
//        when(pair.getCivilizationsArray()).thenReturn(civilizations);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy1);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy2);
//        Diplomacy diplomacy = GameController.getGameController().getWarInfos(pair);
//        Assertions.assertNull(diplomacy);
//    }
//
//    public ArrayList<DiplomaticRelation> getDiplomaticRelationsMapOfCivilization(Civilization civilization) {
//        ArrayList<DiplomaticRelation> diplomaticRelationsMaps = new ArrayList<>();
//        for (Diplomacy diplomacy : GameDataBase.getGameDataBase().getDiplomaticRelations()) {
//            if (diplomacy instanceof DiplomaticRelation && diplomacy.getPair().containsCivilization(civilization))
//                diplomaticRelationsMaps.add((DiplomaticRelation) diplomacy);
//        }
//        return diplomaticRelationsMaps;
//    }
//
//    @Test
//    public void testGetDiplomaticRelationsMapOfCivilization() {
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy1);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy2);
//        ArrayList<DiplomaticRelation> diplomacies = GameController.getGameController().getDiplomaticRelationsMapOfCivilization(civilization1);
//        Assertions.assertEquals(0, diplomacies.size());
//    }
//
//    @Test
//    public void testGetScientificTreatiesOfCivilization() {
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy1);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy2);
//        ArrayList<ScientificTreaty> diplomacies = GameController.getGameController().getScientificTreatiesOfCivilization(civilization1);
//        Assertions.assertEquals(0, diplomacies.size());
//    }
//
//    @Test
//    public void testGetStepWiseGoldTransferContractsOfCivilizationPayer() {
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy1);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy2);
//        ArrayList<StepWiseGoldTransferContract> diplomacies = GameController.getGameController().getStepWiseGoldTransferContractsOfCivilizationPayer(civilization1);
//        Assertions.assertEquals(0, diplomacies.size());
//    }
//
//    @Test
//    public void testGetStepWiseGoldTransferContractsOfCivilizationRecipient() {
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy1);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy2);
//        ArrayList<StepWiseGoldTransferContract> diplomacies = GameController.getGameController().getStepWiseGoldTransferContractsOfCivilizationRecipient(civilization1);
//        Assertions.assertEquals(0, diplomacies.size());
//    }
//
//    @Test
//    public void testGetWarInfoMapOfCivilization() {
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy1);
//        GameDataBase.getGameDataBase().getDiplomaticRelations().add(diplomacy2);
//        ArrayList<WarInfo> diplomacies = GameController.getGameController().getWarInfoMapOfCivilization(civilization1);
//        Assertions.assertEquals(0, diplomacies.size());
//    }
//
//}
