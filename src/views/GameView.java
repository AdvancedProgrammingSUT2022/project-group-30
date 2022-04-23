package views;

import java.util.ArrayList;

import controllers.GameController;
import models.GameMap;
import models.RiverSegment;
import models.Tile;

public class GameView implements View{

    private static GameView gameView;
    private GameController controller = GameController.getGameController();

    private GameView(){

    }

    public static GameView getGameView(){
        return gameView == null ? gameView = new GameView() : gameView;
    }

    public void run(){

    }

    public void showMap()  {
        PrintableCharacters printableCharacters[][] = this.makeMapReadyToPrint();
        for(int i = 0; i < printableCharacters.length; i++){
            for(int j = 0; j < printableCharacters[i].length; j++){
                System.out.print(printableCharacters[i][j].getANSI_COLOR()+printableCharacters[i][j].getCharacter()+PrintableCharacters.ANSI_RESET);
            }
            System.out.println();
        }
    }


    public PrintableCharacters[][] makeMapReadyToPrint()  {
        Tile tiles[][] = this.controller.getGameDataBase().getMap().findTilesToPrint();
        PrintableCharacters printableCharacters[][] = new PrintableCharacters[21][52];
        for(int i = 0; i < 21; i++){
            for(int j = 0; j < 52; j++){
                printableCharacters[i][j] = new PrintableCharacters();
                if(i == 2 && j%16 >= 11 && j <48){
                    printableCharacters[i][j].setCharacter('_');
                }
            }
        }
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                int tileStartingVerticalIndex = (j%2)*3+6*i;
                int tileStartingHorizontalIndex = 2+j*8;
                this.drawATile(printableCharacters, tileStartingVerticalIndex, tileStartingHorizontalIndex, tiles[i][j]);
            }
        }
        this.colorTiles(tiles, printableCharacters);
        this.colorRiverSegments(tiles, printableCharacters);
        return printableCharacters;
    }

    private void colorRiverSegments(Tile tiles[][], PrintableCharacters printableCharacters[][]){
        for(int i = 0 ; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                ArrayList<RiverSegment> riverSegments = GameMap.getGameMap().findTilesRiverSegments(tiles[i][j]);
                for(int k = 0; k < riverSegments.size(); k++){
                    String riverDirection = riverSegments.get(k).findRiverSegmentDirectionForTile(tiles[i][j]);
                    this.colorATileRiverSegment(riverDirection, printableCharacters, j, i);
                }
            }
        }
    }

    private void colorATileRiverSegment(String riverDirection, PrintableCharacters printableCharacters[][], int XIndex, int YIndex){
        int tileStartingVerticalIndex = (XIndex%2)*3+6*YIndex;
        int tileStartingHorizontalIndex = 2+XIndex*8;
        if(riverDirection.equals("RU")){
            printableCharacters[tileStartingVerticalIndex+2][tileStartingHorizontalIndex+8].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex+1][tileStartingHorizontalIndex+7].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex+6].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        }
        else if(riverDirection.equals("RD")){
            printableCharacters[tileStartingVerticalIndex+5][tileStartingHorizontalIndex+6].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex+4][tileStartingHorizontalIndex+7].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex+3][tileStartingHorizontalIndex+8].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        }
        else if(riverDirection.equals("LU")){
            printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex+1][tileStartingHorizontalIndex-1].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex+2][tileStartingHorizontalIndex-2].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        }
        else if(riverDirection.equals("LD")){
            printableCharacters[tileStartingVerticalIndex+3][tileStartingHorizontalIndex-2].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex+4][tileStartingHorizontalIndex-1].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
            printableCharacters[tileStartingVerticalIndex+5][tileStartingHorizontalIndex].setANSI_COLOR(PrintableCharacters.ANSI_BLUE_BACKGROUND_BRIGHT);
        }
    }

    private void colorTiles(Tile tiles[][], PrintableCharacters printableCharacters[][]){
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                int tileStartingVerticalIndex = (j%2)*3+6*i;
                int tileStartingHorizontalIndex = 2+j*8;
                String color = PrintableCharacters.findTilesColor(tiles[i][j]);
                for(int k = 0 ; k < 5; k++){
                    printableCharacters[tileStartingVerticalIndex][tileStartingHorizontalIndex+k+1].setANSI_COLOR(color);
                    printableCharacters[tileStartingVerticalIndex+5][tileStartingHorizontalIndex+k+1].setANSI_COLOR(color);
                }
                for(int k = 0 ; k < 7; k++){
                    printableCharacters[tileStartingVerticalIndex+1][tileStartingHorizontalIndex+k].setANSI_COLOR(color);
                    printableCharacters[tileStartingVerticalIndex+4][tileStartingHorizontalIndex+k].setANSI_COLOR(color);
                }
                for(int k = 0 ; k < 9; k++){
                    printableCharacters[tileStartingVerticalIndex+2][tileStartingHorizontalIndex+k-1].setANSI_COLOR(color);
                    printableCharacters[tileStartingVerticalIndex+3][tileStartingHorizontalIndex+k-1].setANSI_COLOR(color);
                }
            }
        }
    }

    private void drawATile(PrintableCharacters printableCharacters[][] , int i, int j, Tile tile){
        int tileXCoordinate = tile.findTileXCoordinateInMap();
        int tileYCoordinate = tile.findTileYCoordinateInMap();
        printableCharacters[i+1][j+1].setCharacter((char) (tileYCoordinate / 10 + 48));
        printableCharacters[i+1][j+2].setCharacter((char) (tileYCoordinate % 10 + 48));
        printableCharacters[i+1][j+3].setCharacter(',');
        printableCharacters[i+1][j+4].setCharacter((char) (tileYCoordinate / 10 + 48));
        printableCharacters[i+1][j+5].setCharacter((char) (tileYCoordinate % 10 + 48));

        printableCharacters[i][j].setCharacter('/');
        printableCharacters[i+1][j-1].setCharacter('/');
        printableCharacters[i+2][j-2].setCharacter('/');
        printableCharacters[i+3][j-2].setCharacter('\\');
        printableCharacters[i+4][j-1].setCharacter('\\');
        printableCharacters[i+5][j].setCharacter('\\');
        printableCharacters[i+5][j+1].setCharacter('_');
        printableCharacters[i+5][j+2].setCharacter('_');
        printableCharacters[i+5][j+3].setCharacter('_');
        printableCharacters[i+5][j+4].setCharacter('_');
        printableCharacters[i+5][j+5].setCharacter('_');
        printableCharacters[i+5][j+6].setCharacter('/');
        printableCharacters[i+4][j+7].setCharacter('/');
        printableCharacters[i+3][j+8].setCharacter('/');
        printableCharacters[i+2][j+8].setCharacter('\\');
        printableCharacters[i+1][j+7].setCharacter('\\');
        printableCharacters[i][j+6].setCharacter('\\');
    }


    public void showMenu(){

    }
    
    public void setController()  {
        this.controller = GameController.getGameController();
        this.controller.setGameDataBase();
        this.controller.setProgramDatabase();
    }
}
