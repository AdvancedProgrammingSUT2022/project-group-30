package views.images;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import views.Main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Images {

    // buildings
    public static Images ARMORY = new Images("Buildings", "Armory");
    public static Images ARSENAL = new Images("Buildings", "Arsenal");
    public static Images BANK = new Images("Buildings", "Bank");
    public static Images BARRACKS = new Images("Buildings", "Barracks");
    public static Images BROADCAST_TOWER = new Images("Buildings", "Broadcast Tower");
    public static Images BURIAL_TOMB = new Images("Buildings", "Burial Tomb");
    public static Images CASTLE = new Images("Buildings", "Castle");
    public static Images CIRCUS = new Images("Buildings", "Circus");
    public static Images COLOSSEUM = new Images("Buildings", "Colosseum");
    public static Images COURTHOUSE = new Images("Buildings", "Courthouse");
    public static Images FACTORY = new Images("Buildings", "Factory");
    public static Images FORGE = new Images("Buildings", "Forge");
    public static Images GARDEN = new Images("Buildings", "Garden");
    public static Images GRANARY = new Images("Buildings", "Granary");
    public static Images HOSPITAL = new Images("Buildings", "Hospital");
    public static Images LIBRARY = new Images("Buildings", "Library");
    public static Images MARKET = new Images("Buildings", "Market");
    public static Images MILITARY_ACADEMY = new Images("Buildings", "Military Academy");
    public static Images MILITARY_BASE = new Images("Buildings", "Military Base");
    public static Images MINT = new Images("Buildings", "Mint");
    public static Images MONASTERY = new Images("Buildings", "Monastery");
    public static Images MONUMENT = new Images("Buildings", "Monument");
    public static Images MUSEUM = new Images("Buildings", "Museum");
    public static Images OPERA_HOUSE = new Images("Buildings", "Opera House");
    public static Images PALACE = new Images("Buildings", "Palace");
    public static Images PUBLIC_SCHOOL = new Images("Buildings", "Public School");
    public static Images SATRAPS_COURT = new Images("Buildings", "Satrap's Court");
    public static Images STABLE = new Images("Buildings", "Stable");
    public static Images STOCK_EXCHANGE = new Images("Buildings", "Stock Exchange");
    public static Images TEMPLE = new Images("Buildings", "Temple");
    public static Images THEATRE = new Images("Buildings", "Theatre");
    public static Images UNIVERSITY = new Images("Buildings", "University");
    public static Images WALLS = new Images("Buildings", "Walls");
    public static Images WATER_MILL = new Images("Buildings", "Water Mill");
    public static Images WINDMILL = new Images("Buildings", "Windmill");
    public static Images WORKSHOP = new Images("Buildings", "Workshop");

    // features
    public static Images FLOOD_PLAINS = new Images("Features", "Flood Plains");
    public static Images FOREST = new Images("Features", "Forest");
    public static Images ICE = new Images("Features", "Ice");
    public static Images JUNGLE = new Images("Features", "Jungle");
    public static Images MARSH = new Images("Features", "Marsh");
    public static Images OASIS = new Images("Features", "Oasis");

    // improvements
    public static Images CAMP = new Images("improvements", "Camp");
    public static Images FARM = new Images("improvements", "Farm");
    public static Images LUMBER_MILL = new Images("improvements", "Lumber Mill");
    public static Images MANUFACTORY = new Images("improvements", "Manufactory");
    public static Images MINE = new Images("improvements", "Mine");
    public static Images PASTURE = new Images("improvements", "Pasture");
    public static Images PLANTATION = new Images("improvements", "Plantation");
    public static Images QUARRY = new Images("improvements", "Quarry");
    public static Images RAIL_ROAD = new Images("improvements", "Rail Road");
    public static Images ROAD = new Images("improvements", "Road");
    public static Images TRADING_POST = new Images("improvements", "Trading Post");

    // resources
    public static Images BANANA = new Images("Resources", "Banana");
    public static Images COAL = new Images("Resources", "Coal");
    public static Images COTTON = new Images("Resources", "Cotton");
    public static Images COW = new Images("Resources", "Cow");
    public static Images DYE = new Images("Resources", "Dye");
    public static Images FUR = new Images("Resources", "Fur");
    public static Images GAZELLE = new Images("Resources", "Gazelle");
    public static Images GEM = new Images("Resources", "Gem");
    public static Images GOLD = new Images("Resources", "Gold");
    public static Images HORSE = new Images("Resources", "Horse");
    public static Images INCENSE = new Images("Resources", "Incense");
    public static Images IRON = new Images("Resources", "Iron");
    public static Images IVORY = new Images("Resources", "Ivory");
    public static Images MARBLE = new Images("Resources", "Marble");
    public static Images SHEEP = new Images("Resources", "Sheep");
    public static Images SILK = new Images("Resources", "Silk");
    public static Images SILVER = new Images("Resources", "Silver");
    public static Images SUGAR = new Images("Resources", "Sugar");
    public static Images WHEAT = new Images("Resources", "Wheat");

    // terrain types
    public static Images DESERT = new Images("Terrain Types", "Desert");
    public static Images FOG_OF_WAR = new Images("Terrain Types", "FogOfWar");
    public static Images GRASSLAND = new Images("Terrain Types", "Grassland");
    public static Images HILLS = new Images("Terrain Types", "Hills");
    public static Images MOUNTAIN = new Images("Terrain Types", "Mountain");
    public static Images OCEAN = new Images("Terrain Types", "Ocean");
    public static Images PLAINS = new Images("Terrain Types", "Plains");
    public static Images RIVER_BOTTOM = new Images("Terrain Types", "River-Bottom");
    public static Images RIVER_BOTTOM_LEFT = new Images("Terrain Types", "River-BottomLeft");
    public static Images RIVER_BOTTOM_RIGHT = new Images("Terrain Types", "River-BottomRight");
    public static Images SNOW = new Images("Terrain Types", "Snow");
    public static Images TUNDRA = new Images("Terrain Types", "Tundra");


    // units
    public static Images ANTI_TANK_GUN = new Images("Units3", "Anti-Tank Gun");
    public static Images ARCHER = new Images("Units3", "Archer");
    public static Images ARTILLERY = new Images("Units3", "Artillery");
    public static Images CANNON = new Images("Units3", "Cannon");
    public static Images CATAPULT = new Images("Units3", "Catapult");
    public static Images CAVALRY = new Images("Units3", "Cavalry");
    public static Images CHARIOT_ARCHER = new Images("Units3", "Chariot Archer");
    public static Images CROSSBOWMAN = new Images("Units3", "Crossbowman");
    public static Images HORSEMAN = new Images("Units3", "Horseman");
    public static Images INFANTRY = new Images("Units3", "Infantry");
    public static Images KNIGHT = new Images("Units3", "Knight");
    public static Images LANCER = new Images("Units3", "Lancer");
    public static Images LONGSWORDSMAN = new Images("Units3", "Longswordsman");
    public static Images MUSKETMAN = new Images("Units3", "Musketman");
    public static Images PANZER = new Images("Units3", "Panzer");
    public static Images PIKEMAN= new Images("Units3", "Pikeman");
    public static Images RIFLEMAN = new Images("Units3", "Rifleman");
    public static Images SCOUT = new Images("Units3", "Scout");
    public static Images SETTLER = new Images("Units3", "Settler");
    public static Images SPEARMAN = new Images("Units3", "Spearman");
    public static Images SWORDSMAN = new Images("Units3", "Swordsman");
    public static Images TANK = new Images("Units3", "Tank");
    public static Images TREBUCHET = new Images("Units3", "Trebuchet");
    public static Images WARRIOR = new Images("Units3", "Warrior");
    public static Images WORKER = new Images("Units3", "Worker");




    private static ArrayList<Images> allImages = new ArrayList<>();

    private ImagePattern imagePattern;
    private String name;

    public Images(String directoryName, String name){
        this.name = name;
        try {
            this.imagePattern = new ImagePattern(new Image(new URL(Main.class.getResource("/images/" + directoryName + "/" + name + ".png").toExternalForm()).toExternalForm()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        allImages.add(this);
    }

    public String getName(){
        return this.name;
    }

    public ImagePattern getImagePattern(){
        return this.imagePattern;
    }

    public static ImagePattern getImage(String info){
        for(int i = 0; i < allImages.size(); i++){
            if(info.equals(allImages.get(i).getName())){
                return allImages.get(i).getImagePattern();
            }
        }
        return null;
    }
}
