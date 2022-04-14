package battleships.player;

public class Player {

    private String name;
    protected Board board = new Board();
    protected Board enemyBoardView = new Board();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public Board getEnemyBoardView() {
        return enemyBoardView;
    }

    public String getMergedBoards() {
        return String.format("%s---------------------%n%s", enemyBoardView, board);
    }
}
