package battleships.console;

import battleships.coordinates.Coordinates;
import java.util.Scanner;

class ConsoleInput {

    private static final Scanner SCANNER = new Scanner(System.in);

    static void passMoveToAI() {
        System.out.println("Press Enter to end your turn...");
        SCANNER.nextLine();
        System.out.println("\n");
    }

    static void passMoveToHuman(String playerName, String newPlayerName) {
        System.out.println(playerName + " press Enter to end your turn...");
        SCANNER.nextLine();
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
        System.out.println(newPlayerName + " press Enter to start Your turn...");
        SCANNER.nextLine();
    }

    static Coordinates[] getShipEndsCoordinates() {
        while (true) {
            System.out.print("> ");
            String[] rawCoordinates = (SCANNER.next() + SCANNER.nextLine()).trim().split("\\s+");
            try {
                String shipBegin = rawCoordinates[0];
                int beginRow = shipBegin.toUpperCase().charAt(0) - 64;
                int beginColumn = Integer.parseInt(shipBegin.substring(1));
                String shipEnd = rawCoordinates[1];
                int endRow = shipEnd.toUpperCase().charAt(0) - 64;
                int endColumn = Integer.parseInt(shipEnd.substring(1));
                return new Coordinates[]{new Coordinates(beginRow, beginColumn), new Coordinates(endRow, endColumn)};
            } catch (Exception exception) {
                System.out.println("Could not parse coordinates. Correct input example: 'A1 D1', 'A1 a5', 'c1 c3'. Try again");
            }
        }
    }

    static Coordinates getSingleCoordinates() {
        while (true) {
            System.out.print("> ");
            String input = SCANNER.next() + SCANNER.nextLine();
            try {
                int row = input.toUpperCase().charAt(0) - 64;
                int column = Integer.parseInt(input.substring(1));
                return new Coordinates(row, column);
            } catch (NumberFormatException exception) {
                System.out.println("Could not parse input, try again");
            }
        }
    }

    static int getInt() {
        while (true) {
            System.out.print("> ");
            try {
                return SCANNER.nextInt();
            } catch (Exception exception) {
                SCANNER.nextLine();
                System.out.println("Could not parse input, try again:");
            }
        }
    }
}
