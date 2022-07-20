package models;

public class RiverSegment {
    private final Tile firstTile;
    private final Tile secondTile;

    public RiverSegment(Tile firstTile, Tile secondTile) {
        this.firstTile = firstTile;
        this.secondTile = secondTile;
    }

    public String findRiverSegmentDirectionForTile(Tile tile) {
        if (tile == firstTile) {
            return findRiverSegmentDirectionForFirstTile();
        } else {
            return findRiverSegmentDirectionForSecondTile();
        }
    }

    //bellow two functions return : LU -> left up, LD -> left down, RU -> right up, RD -> right down
    private String findRiverSegmentDirectionForFirstTile() {
        if (this.firstTile.findTileXCoordinateInMap() % 2 == 0) {
            if (this.secondTile.findTileXCoordinateInMap() > this.firstTile.findTileXCoordinateInMap()) {
                if (this.secondTile.findTileYCoordinateInMap() == this.firstTile.findTileYCoordinateInMap()) {
                    return "RD";
                } else {
                    return "RU";
                }
            } else {
                if (this.secondTile.findTileYCoordinateInMap() == this.firstTile.findTileYCoordinateInMap()) {
                    return "LD";
                } else {
                    return "LU";
                }
            }
        } else {
            if (this.secondTile.findTileXCoordinateInMap() > this.firstTile.findTileXCoordinateInMap()) {
                if (this.secondTile.findTileYCoordinateInMap() == this.firstTile.findTileYCoordinateInMap()) {
                    return "RU";
                } else {
                    return "RD";
                }
            } else {
                if (this.secondTile.findTileYCoordinateInMap() == this.firstTile.findTileYCoordinateInMap()) {
                    return "LU";
                } else {
                    return "LD";
                }
            }
        }
    }

    private String findRiverSegmentDirectionForSecondTile() {
        if (this.secondTile.findTileXCoordinateInMap() % 2 == 0) {
            if (this.firstTile.findTileXCoordinateInMap() > this.secondTile.findTileXCoordinateInMap()) {
                if (this.firstTile.findTileYCoordinateInMap() == this.secondTile.findTileYCoordinateInMap()) {
                    return "RD";
                } else {
                    return "RU";
                }
            } else {
                if (this.firstTile.findTileYCoordinateInMap() == this.secondTile.findTileYCoordinateInMap()) {
                    return "LD";
                } else {
                    return "LU";
                }
            }
        } else {
            if (this.firstTile.findTileXCoordinateInMap() > this.secondTile.findTileXCoordinateInMap()) {
                if (this.firstTile.findTileYCoordinateInMap() == this.secondTile.findTileYCoordinateInMap()) {
                    return "RU";
                } else {
                    return "RD";
                }
            } else {
                if (this.firstTile.findTileYCoordinateInMap() == this.secondTile.findTileYCoordinateInMap()) {
                    return "LU";
                } else {
                    return "LD";
                }
            }
        }
    }

    public static boolean checkTilesCoordinatesValidity(int firstTileXCoordinate, int firstTileYCoordinate, int secondTileXCoordinate, int secondTileYCoordinate) {
        if (firstTileXCoordinate % 2 == 0) {
            return checkTilesCoordinatesValidityWithFirstTileEvenXCoordinate(firstTileXCoordinate, firstTileYCoordinate, secondTileXCoordinate, secondTileYCoordinate);
        } else {
            return checkTilesCoordinatesValidityWithFirstTileOddXCoordinate(firstTileXCoordinate, firstTileYCoordinate, secondTileXCoordinate, secondTileYCoordinate);
        }
    }

    private static boolean checkTilesCoordinatesValidityWithFirstTileOddXCoordinate(int firstTileXCoordinate, int firstTileYCoordinate, int secondTileXCoordinate, int secondTileYCoordinate) {
        if (secondTileYCoordinate == firstTileYCoordinate - 1 && secondTileXCoordinate == firstTileXCoordinate) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate + 1 && secondTileXCoordinate == firstTileXCoordinate) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate && secondTileXCoordinate == firstTileXCoordinate - 1) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate && secondTileXCoordinate == firstTileXCoordinate + 1) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate + 1 && secondTileXCoordinate == firstTileXCoordinate - 1) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate + 1 && secondTileXCoordinate == firstTileXCoordinate + 1) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkTilesCoordinatesValidityWithFirstTileEvenXCoordinate(int firstTileXCoordinate, int firstTileYCoordinate, int secondTileXCoordinate, int secondTileYCoordinate) {
        if (secondTileYCoordinate == firstTileYCoordinate - 1 && secondTileXCoordinate == firstTileXCoordinate) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate + 1 && secondTileXCoordinate == firstTileXCoordinate) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate && secondTileXCoordinate == firstTileXCoordinate - 1) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate && secondTileXCoordinate == firstTileXCoordinate + 1) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate - 1 && secondTileXCoordinate == firstTileXCoordinate - 1) {
            return true;
        } else if (secondTileYCoordinate == firstTileYCoordinate - 1 && secondTileXCoordinate == firstTileXCoordinate + 1) {
            return true;
        } else {
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
