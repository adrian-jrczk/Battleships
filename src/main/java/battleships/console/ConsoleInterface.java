package battleships.console;

import battleships.coordinates.Coordinates;
import battleships.coordinates.InvalidCoordinatesException;
import battleships.core.Engine;
import battleships.core.ShipType;
import battleships.player.AIPlayer;
import battleships.player.Board;
import battleships.player.Player;

public class ConsoleInterface {

    private final Engine engine = new Engine();

    public void open() {
        System.out.println("""
                
                Welcome to the Battleships
                Choose game mode:
                1. Human vs AI
                2. Human vs Human
                """);
        while (true) {
            switch (ConsoleInput.getInt()) {
                case 1 -> humanVsAIMode(new Player("Player"), new AIPlayer("Artificial Adversary"));
                case 2 -> humanVsHumanMode(new Player("Player 1"), new Player("Player 2"));
                default -> {
                    System.out.println("Wrong number, try again");
                    continue;
                }
            }
            return;
        }
    }

    private void humanVsAIMode(Player player, AIPlayer computerPlayer) {
        humanShipPlacement(player);
        ConsoleInput.passMoveToAI();
        AIShipPlacement(computerPlayer);
        while (true) {
            humanMoveTurn(player, computerPlayer);
            ConsoleInput.passMoveToAI();
            AIMoveTurn(computerPlayer, player);
        }
    }

    private void humanVsHumanMode(Player player1, Player player2) {
        humanShipPlacement(player1);
        ConsoleInput.passMoveToHuman(player1.getName(), player2.getName());
        humanShipPlacement(player2);
        ConsoleInput.passMoveToHuman(player2.getName(), player1.getName());
        while (true) {
            humanMoveTurn(player1, player2);
            ConsoleInput.passMoveToHuman(player1.getName(), player2.getName());
            humanMoveTurn(player2, player1);
            ConsoleInput.passMoveToHuman(player2.getName(), player1.getName());
        }
    }

    private void humanMoveTurn(Player currentPlayer, Player otherPlayer) {
        System.out.printf("%n%s%n%s, it's your turn:%n", currentPlayer.getMergedBoards(), currentPlayer.getName());
        while (true) {
            Coordinates coordinates = ConsoleInput.getSingleCoordinates();
            if (engine.coordinatesAreOutOfField(coordinates)) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }
            switch (engine.applyShot(coordinates, currentPlayer, otherPlayer)) {
                case SUCCESSFUL_HIT -> System.out.println("You hit a ship!");
                case MISSED -> System.out.println("You missed!");
                case WHOLE_SHIP_WAS_SUNK -> System.out.println("You sank a ship!");
                case LAST_SHIP_WAS_SUNK -> {
                    System.out.printf("%s, You sank the last ship and therefore You won. Congratulations!%n", currentPlayer.getName());
                    System.exit(0);
                }
            }
            return;
        }
    }

    private void AIMoveTurn(AIPlayer computerPlayer, Player otherPlayer) {
        System.out.printf("%n%s: My turn%n", computerPlayer.getName());
        Coordinates hitSpot = computerPlayer.generateTargetCoordinates();
        char row = (char) (hitSpot.row + 64);
        System.out.printf("%s: My target is %s%d%n", computerPlayer.getName(), row, hitSpot.col);
        switch (engine.applyShot(hitSpot, computerPlayer, otherPlayer)) {
            case SUCCESSFUL_HIT:
                System.out.printf("%s: I hit a ship!%n", computerPlayer.getName());
                computerPlayer.processSuccessfulHit(hitSpot);
                break;
            case MISSED:
                System.out.printf("%s: I missed...%n", computerPlayer.getName());
                break;
            case WHOLE_SHIP_WAS_SUNK:
                System.out.printf("%s: I sank Your ship!%n", computerPlayer.getName());
                computerPlayer.processShipDestruction(hitSpot);
                break;
            case LAST_SHIP_WAS_SUNK:
                System.out.printf("%s: I sank Your last ship so I won, EASY PEASY!%n", computerPlayer.getName());
                System.exit(0);
        }
    }

    private void humanShipPlacement(Player player) {
        System.out.printf("%n%s, place your ships on the board%n", player.getName());
        for (ShipType shipType : ShipType.values()) {
            System.out.println(player.getBoard());
            Coordinates[] coordinates = getShipCoordinates(player.getBoard(), shipType);
            engine.placeShip(player.getBoard(), coordinates);
        }
        System.out.println(player.getBoard());
    }

    private void AIShipPlacement(AIPlayer player) {
        System.out.printf("%n%s: Generating coordinates of my ships...%n", player.getName());
        for (ShipType shipType : ShipType.values()) {
            Coordinates[] coordinates = getShipCoordinatesFromAI(player, shipType);
            engine.placeShip(player.getBoard(), coordinates);
        }
        System.out.printf("%s: Done!%n%n", player.getName());
    }

    private Coordinates[] getShipCoordinatesFromAI(AIPlayer player, ShipType shipType) {
        while (true) {
            Coordinates[] shipEnds = player.generateShipEndsCoordinates(shipType);
            try {
                Coordinates[] coordinates = engine.fillShipCoordinates(shipEnds);
                engine.validateShipCoordinates(player.getBoard(), coordinates, shipType);
                return coordinates;
            } catch (InvalidCoordinatesException ignore) {
            }
        }
    }

    private Coordinates[] getShipCoordinates(Board board, ShipType shipType) {
        while (true) {
            System.out.printf("Enter the coordinates of the %s (%d cells)\n", shipType.name, shipType.length);
            Coordinates[] shipEnds = ConsoleInput.getShipEndsCoordinates();
            try {
                Coordinates[] coordinates = engine.fillShipCoordinates(shipEnds);
                engine.validateShipCoordinates(board, coordinates, shipType);
                return coordinates;
            } catch (InvalidCoordinatesException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}