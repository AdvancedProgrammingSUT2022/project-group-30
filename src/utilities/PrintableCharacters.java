package utilities;

import models.TerrainType;
import models.Tile;

public class PrintableCharacters {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED_BACKGROUND = "\033[41m"; // for mountain
    public static final String ANSI_BLUE_BACKGROUND = "\033[44m"; // for ocean
    public static final String ANSI_YELLOW_BACKGROUND = "\033[43m"; //for plains
    public static final String ANSI_WHITE_BACKGROUND = "\033[47m"; // for snow
    public static final String ANSI_CYAN_BACKGROUND = "\033[46m"; // for tundra
    public static final String ANSI_PURPLE_BACKGROUND = "\033[45m"; // for hills
    public static final String ANSI_GREEN_BACKGROUND = "\033[42m"; // for grassland
    public static final String ANSI_RED_BACKGROUND_BRIGHT = "\033[0;101m"; // for desert
    public static final String ANSI_BLUE_BACKGROUND_BRIGHT = "\033[0;104m"; // for river segments

    private char character;
    private String ANSI_COLOR;

    public PrintableCharacters(){
        this.character = ' ';
        this.ANSI_COLOR = ANSI_RESET;
    }

    public static String findTilesColor(Tile tile){
        if(tile.getTerrainType() == TerrainType.MOUNTAIN){
            return ANSI_RED_BACKGROUND;
        }
        else if(tile.getTerrainType() == TerrainType.OCEAN){
            return ANSI_BLUE_BACKGROUND;
        }
        else if(tile.getTerrainType() == TerrainType.PLAINS){
            return ANSI_YELLOW_BACKGROUND;
        }
        else if(tile.getTerrainType() == TerrainType.SNOW){
            return ANSI_WHITE_BACKGROUND;
        }
        else if(tile.getTerrainType() == TerrainType.TUNDRA){
            return ANSI_CYAN_BACKGROUND;
        }
        else if(tile.getTerrainType() == TerrainType.HILLS){
            return ANSI_PURPLE_BACKGROUND;
        }
        else if(tile.getTerrainType() == TerrainType.GRASSLAND){
            return ANSI_GREEN_BACKGROUND;
        }
        else if(tile.getTerrainType() == TerrainType.DESERT){
            return ANSI_RED_BACKGROUND_BRIGHT;
        }
        return null;
    }

    public void setCharacter(char character){
        this.character = character;
    }

    public char getCharacter(){
        return this.character;
    }

    public void setANSI_COLOR(String ANSI_COLOR){
        this.ANSI_COLOR = ANSI_COLOR;
    }

    public String getANSI_COLOR(){
        return this.ANSI_COLOR;
    }
    
}
