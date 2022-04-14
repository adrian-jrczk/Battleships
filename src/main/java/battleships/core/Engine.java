package battleships.core;

import battleships.coordinates.Coordinates;
import battleships.coordinates.InvalidCoordinatesException;
import battleships.player.Board;
import battleships.player.Player;

public class Engine {

    public Coordinates[] fillShipCoordinates(Coordinates[] shipEnds) throws InvalidCoordinatesException {
        if (shipEnds[0].row != shipEnds[1].row && shipEnds[0].col != shipEnds[1].col) {
            throw new InvalidCoordinatesException("Ship's end must be in the same row or column, try again");
        }
        for (Coordinates coordinates : shipEnds) {
            if (coordinates.row > 10 || coordinates.row < 1 || coordinates.col > 10 || coordinates.col < 1) {
                throw new InvalidCoordinatesException("Coordinates indicate spot outside the field, try again");
            }
        }
        boolean rowsAreEqual = shipEnds[0].row == shipEnds[1].row;
        int row = rowsAreEqual ? shipEnds[0].row : Math.min(shipEnds[0].row, shipEnds[1].row);
        int col = !rowsAreEqual ? shipEnds[0].col : Math.min(shipEnds[0].col, shipEnds[1].col);
        Coordinates[] coordinates = new Coordinates[Math.abs((shipEnds[0].row - shipEnds[1].row) - (shipEnds[0].col - shipEnds[1].col)) + 1];
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = rowsAreEqual ? new Coordinates(row, col + i) : new Coordinates(row + i, col);
        }
        return coordinates;
    }

    public boolean coordinatesAreOutOfField(Coordinates coordinates) {
        return coordinates.row > 10 || coordinates.row < 1 || coordinates.col > 10 || coordinates.col < 1;
    }

    public void validateShipCoordinates(Board board, Coordinates[] coordinates, ShipType shipType) throws InvalidCoordinatesException {
        if (coordinates.length != shipType.length) {
            throw new InvalidCoordinatesException(String.format("Error! Wrong length of the %s! Try again:", shipType.name));
        }
        for (Coordinates coordinate : coordinates) {
            if (spotIsAdjacentToShip(board, coordinate.row, coordinate.col)) {
                throw new InvalidCoordinatesException("Error! You placed it too close to another one. Try again:");
            }
        }
    }

    public void placeShip(Board board, Coordinates[] coordinates) {
        for (Coordinates coordinate : coordinates) {
            board.put("O", coordinate.row, coordinate.col);
        }
    }

    public ShotResult applyShot(Coordinates coordinates, Player shootingPlayer, Player otherPlayer) {
        String targetElement = otherPlayer.getBoard().get(coordinates.row, coordinates.col);
        switch (targetElement) {
            case "X":
                return ShotResult.TARGET_ALREADY_DESTROYED;
            case "~":
                shootingPlayer.getEnemyBoardView().put("M", coordinates.row, coordinates.col);
                return ShotResult.MISSED;
            case "O":
                shootingPlayer.getEnemyBoardView().put("X", coordinates.row, coordinates.col);
                otherPlayer.getBoard().put("X", coordinates.row, coordinates.col);
                if (gameIsFinished(otherPlayer.getBoard())) {
                    return ShotResult.LAST_SHIP_WAS_SUNK;
                } else {
                    if (shipIsNotSunk(otherPlayer.getBoard(), coordinates.row, coordinates.col, 0)) {
                        return ShotResult.SUCCESSFUL_HIT;
                    } else {
                        return ShotResult.WHOLE_SHIP_WAS_SUNK;
                    }
                }
            default:
                return ShotResult.UNKNOWN;
        }
    }

    private boolean shipIsNotSunk(Board board, int row, int col, int directionControlNum) {
        if (row > 10 || row < 1 || col > 10 || col < 1) {
            return false;
        }
        String spotElement = board.get(row, col);
        if (spotElement.equals("O")) {
            return true;
        } else if (spotElement.equals("~")) {
            return false;
        }
        switch (directionControlNum) {
            case 1:
                return shipIsNotSunk(board, row, col - 1, 1);
            case 2:
                return shipIsNotSunk(board, row - 1, col, 2);
            case 3:
                return shipIsNotSunk(board, row, col + 1, 3);
            case 4:
                return shipIsNotSunk(board, row + 1, col, 4);
            case 0:
                return shipIsNotSunk(board, row, col - 1, 1) ||
                        shipIsNotSunk(board, row - 1, col, 2) ||
                        shipIsNotSunk(board, row, col + 1, 3) ||
                        shipIsNotSunk(board, row + 1, col, 4);
            default:
                return false;
        }
    }

    public boolean gameIsFinished(Board board) {
        return !board.toString().contains("O");
    }

    private boolean spotIsAdjacentToShip(Board board, int spotRow, int spotCol) {
        for (int row = spotRow - 1; row < spotRow + 2; row++) {
            if (row < 1 || row > 10) {
                continue;
            }
            for (int col = spotCol - 1; col < spotCol + 2; col++) {
                if (col < 1 || col > 10) {
                    continue;
                }
                if (board.get(row, col).equals("O")) {
                    return true;
                }
            }
        }
        return false;
    }
}