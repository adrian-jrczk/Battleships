package battleships.player;

public class Board {

    private final String[][] BOARD = new String[][]{
            {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"A", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"B", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"C", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"D", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"E", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"F", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"G", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"H", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"I", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"},
            {"J", "~", "~", "~", "~", "~", "~", "~", "~", "~", "~"}
    };

    public void put(String element, int row, int col) {
        BOARD[row][col] = element;
    }

    public String get(int row, int col) {
        return BOARD[row][col];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String[] row : BOARD) {
            for (String element : row) {
                builder.append(element).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}