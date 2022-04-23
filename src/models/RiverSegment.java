package models;

public class RiverSegment {
    private final Tile firstTile;
    private final Tile secondTile;

    public RiverSegment(Tile firstTile, Tile secondTile) {
        this.firstTile = firstTile;
        this.secondTile = secondTile;
    }

    public static boolean checkTilesCoordinatesValidity(int firstTileXCoordinate, int firstTileYCoordinate, int secondTileXCoordinate, int secondTileYCoordinate){
        if(firstTileXCoordinate % 2 == 0){
            return checkTilesCoordinatesValidityWithFirstTileEvenXCoordinate(firstTileXCoordinate, firstTileYCoordinate, secondTileXCoordinate, secondTileYCoordinate);
        }
        else{
            return checkTilesCoordinatesValidityWithFirstTileOddXCoordinate(firstTileXCoordinate, firstTileYCoordinate, secondTileXCoordinate, secondTileYCoordinate);
        }
    }

    private static boolean checkTilesCoordinatesValidityWithFirstTileOddXCoordinate(int firstTileXCoordinate, int firstTileYCoordinate, int secondTileXCoordinate, int secondTileYCoordinate){
        if(secondTileYCoordinate == firstTileYCoordinate-1 && secondTileXCoordinate == firstTileXCoordinate){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate+1 && secondTileXCoordinate == firstTileXCoordinate){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate && secondTileXCoordinate == firstTileXCoordinate-1){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate && secondTileXCoordinate == firstTileXCoordinate+1){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate+1 && secondTileXCoordinate == firstTileXCoordinate-1){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate+1 && secondTileXCoordinate == firstTileXCoordinate+1){
            return true;
        }
        else{
            return false;
        }
    }

    private static boolean checkTilesCoordinatesValidityWithFirstTileEvenXCoordinate(int firstTileXCoordinate, int firstTileYCoordinate, int secondTileXCoordinate, int secondTileYCoordinate){
        if(secondTileYCoordinate == firstTileYCoordinate-1 && secondTileXCoordinate == firstTileXCoordinate){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate+1 && secondTileXCoordinate == firstTileXCoordinate){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate && secondTileXCoordinate == firstTileXCoordinate-1){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate && secondTileXCoordinate == firstTileXCoordinate+1){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate-1 && secondTileXCoordinate == firstTileXCoordinate-1){
            return true;
        }
        else if(secondTileYCoordinate == firstTileYCoordinate-1 && secondTileXCoordinate == firstTileXCoordinate+1){
            return true;
        }
        else{
            return false;
        }
    }

    public Tile getFirstTile() {
        return this.firstTile;
    }

    public Tile getSecondTile() {
        return this.secondTile;
    }
}

