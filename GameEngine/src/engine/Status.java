package engine;

public class Status {
    private int leftTiles;
    private String playerName;
    private char[][] board;

    Status(char[][] board, String playerName, int leftTiles) {
        this.board = board;
        this.leftTiles = leftTiles;
        this.playerName = playerName;
    }

    public int getLeftTiles() {
        return leftTiles;
    }

    public String getPlayerName() {
        return playerName;
    }

    public char[][] getBoard() {
        return board;
    }
}
