package battleships.player;

import battleships.coordinates.Coordinates;
import battleships.core.ShipType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer extends Player {

    private Random random = new Random();
    private List<Coordinates> huntedShipCoordinates = new ArrayList<>();

    public AIPlayer(String name) {
        super(name);
    }

    public Coordinates[] generateShipEndsCoordinates(ShipType shipType) {
        int freeCellsNeeded = shipType.length - 1;
        while (true) {
            int startRow = 1 + random.nextInt(10);
            int startCol = 1 + random.nextInt(10);
            int randomDirection = 1 + random.nextInt(4); //1-left 2-up 3-right 4-down
            switch (randomDirection) {
                case 1:
                    if (canPlaceShipToTheLeft(freeCellsNeeded, startRow, startCol)) {
                        return new Coordinates[]{new Coordinates(startRow, startCol), new Coordinates(startRow, startCol - freeCellsNeeded)};
                    }
                    break;
                case 2:
                    if (canPlaceShipUpwards(freeCellsNeeded, startRow, startCol)) {
                        return new Coordinates[]{new Coordinates(startRow, startCol), new Coordinates(startRow - freeCellsNeeded, startCol)};
                    }
                    break;
                case 3:
                    if (canPlaceShipToTheRight(freeCellsNeeded, startRow, startCol)) {
                        return new Coordinates[]{new Coordinates(startRow, startCol), new Coordinates(startRow, startCol + freeCellsNeeded)};
                    }
                    break;
                case 4:
                    if (canPlaceShipDownwards(freeCellsNeeded, startRow, startCol)) {
                        return new Coordinates[]{new Coordinates(startRow, startCol), new Coordinates(startRow + freeCellsNeeded, startCol)};
                    }
                    break;
            }
        }
    }

    private boolean canPlaceShipToTheLeft(int freeCellsNeeded, int startRow, int startCol) {
        for (int col = startCol - freeCellsNeeded; col <= startCol; col++) {
            if (col < 1) {
                return false;
            }
            if (!board.get(startRow, col).equals("~")) {
                return false;
            }
        }
        return true;
    }

    private boolean canPlaceShipUpwards(int freeCellsNeeded, int startRow, int startCol) {
        for (int row = startRow - freeCellsNeeded; row <= startRow; row++) {
            if (row < 1) {
                return false;
            }
            if (!board.get(row, startCol).equals("~")) {
                return false;
            }
        }
        return true;
    }

    private boolean canPlaceShipToTheRight(int freeCellsNeeded, int startRow, int startCol) {
        for (int col = startCol + freeCellsNeeded; col >= startCol; col--) {
            if (col > 10) {
                return false;
            }
            if (!board.get(startRow, col).equals("~")) {
                return false;
            }
        }
        return true;
    }

    private boolean canPlaceShipDownwards(int freeCellsNeeded, int startRow, int startCol) {
        for (int row = startRow + freeCellsNeeded; row >= startRow; row--) {
            if (row > 10) {
                return false;
            }
            if (!board.get(row, startCol).equals("~")) {
                return false;
            }
        }
        return true;
    }

    public Coordinates generateTargetCoordinates() {

        if (huntedShipCoordinates.isEmpty()) {
            while (true) {
                int randomRow = 1 + random.nextInt(10);
                int randomCol = 1 + random.nextInt(10);
                if (enemyBoardView.get(randomRow, randomCol).equals("~")) {
                    return new Coordinates(randomRow, randomCol);
                }
            }
        }

        if (huntedShipCoordinates.size() == 1) {
            Coordinates hitSpot = huntedShipCoordinates.get(0);
            return findPotentialNeighbouringShipSpot(hitSpot.row, hitSpot.col);
        }

        boolean isRowShip = huntedShipCoordinates.get(0).row == huntedShipCoordinates.get(1).row;

        if (isRowShip) {
            int leftEndColumn = 10;
            for (Coordinates coordinates : huntedShipCoordinates) {
                if (coordinates.col < leftEndColumn) {
                    leftEndColumn = coordinates.col;
                }
            }
            int row = huntedShipCoordinates.get(0).row;
            if (spotIsFree(row, leftEndColumn - 1)) {
                return new Coordinates(row, leftEndColumn - 1);
            } else {
                int rightEndColumn = 0;
                for (Coordinates coordinates : huntedShipCoordinates) {
                    if (coordinates.col > rightEndColumn) {
                        rightEndColumn = coordinates.col;
                    }
                }
                return new Coordinates(row, rightEndColumn + 1);
            }
        } else {
            int upperEndRow = 10;
            for (Coordinates coordinates : huntedShipCoordinates) {
                if (coordinates.row < upperEndRow) {
                    upperEndRow = coordinates.row;
                }
            }
            int col = huntedShipCoordinates.get(0).col;
            if (spotIsFree(upperEndRow - 1, col)) {
                return new Coordinates(upperEndRow - 1, col);
            } else {
                int lowerEndRow = 0;
                for (Coordinates coordinates : huntedShipCoordinates) {
                    if (coordinates.row > lowerEndRow) {
                        lowerEndRow = coordinates.row;
                    }
                }
                return new Coordinates(lowerEndRow + 1, col);
            }
        }
    }

    private Coordinates findPotentialNeighbouringShipSpot(int currRow, int currCol) {
        while (true) {
            int randomDirection = 1 + random.nextInt(4); //1-left 2-up 3-right 4-down
            int row = switch (randomDirection) {
                case 2 -> currRow - 1;
                case 4 -> currRow + 1;
                default -> currRow;
            };
            int col = switch (randomDirection) {
                case 1 -> currCol - 1;
                case 3 -> currCol + 1;
                default -> currCol;
            };
            if (spotIsFree(row, col)) {
                return new Coordinates(row, col);
            }
        }
    }

    private boolean spotIsFree(int spotRow, int spotCol) {
        if (spotRow < 1 || spotRow > 10 || spotCol < 1 || spotCol > 10) {
            return false;
        }
        return enemyBoardView.get(spotRow, spotCol).equals("~");
    }

    public void processSuccessfulHit(Coordinates lastHitCoordinates) {
        huntedShipCoordinates.add(lastHitCoordinates);
    }

    public void processShipDestruction(Coordinates lastHitCoordinates) {
        huntedShipCoordinates.add(lastHitCoordinates);
        for (Coordinates coordinates : huntedShipCoordinates) {
            markSpotAndNeighboursAsEmpty(coordinates.row, coordinates.col);
        }
        huntedShipCoordinates.clear();
    }

    private void markSpotAndNeighboursAsEmpty(int spotRow, int spotCol) {
        for (int row = spotRow - 1; row < spotRow + 2; row++) {
            if (row < 1 || row > 10) {
                continue;
            }
            for (int col = spotCol - 1; col < spotCol + 2; col++) {
                if (col < 1 || col > 10) {
                    continue;
                }
                enemyBoardView.put("E", row, col);
            }
        }
    }
}
