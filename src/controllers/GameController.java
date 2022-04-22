package controllers;

import models.ProgramDatabase;

import models.GameDataBase;
import models.Tile;
import models.works.Work;
import views.PrintableCharacters;

public class GameController {
    private static GameController gameController;

    private GameDataBase gameDataBase = GameDataBase.getGameDataBase();
    private ProgramDatabase programDatabase;

    private GameController() {

    }

    public static GameController getGameController() {
        if (gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    public Tile findWorksLocation(Work work) {  // gets a work and finds the tile that owns it
        // TODO
        return null;
    }

    public PrintableCharacters[][] makeMapReadyToPrint()  {
        Tile tiles[][] = this.gameDataBase.getMap().findTilesToPrint();
        PrintableCharacters printableCharacters[][] = new PrintableCharacters[21][52];
        for(int i = 0; i < 21; i++){
            for(int j = 0; j < 52; j++){
                printableCharacters[i][j] = new PrintableCharacters();
            }
        }
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                int tileStartingVerticalIndex = (j%2)*3+6*i;
                int tileStartingHorizontalIndex = 2+j*8;
                this.drawATile(printableCharacters, tileStartingVerticalIndex, tileStartingHorizontalIndex);
            }
        }
        this.colorTiles(tiles, printableCharacters);
        return printableCharacters;
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

    private void drawATile(PrintableCharacters printableCharacters[][] , int i, int j){
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

    public void setProgramDatabase(){
        this.programDatabase = ProgramDatabase.getProgramDatabase();
    }

    public ProgramDatabase getProgramDatabase(){
        return this.programDatabase;
    }

    public void setGameDataBase()  {
        this.gameDataBase = GameDataBase.getGameDataBase();
    }

    public GameDataBase getGameDataBase(){
        return this.gameDataBase;
    }
}
