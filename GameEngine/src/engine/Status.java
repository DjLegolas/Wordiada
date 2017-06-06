package engine;

public class Status {
    private int leftTiles;
    private String playerName;
    private short playerId;
    private float score;
    private char[][] board;

    Status(char[][] board, String playerName, int leftTiles, float score, short playerId) {
        this.board = board;
        this.leftTiles = leftTiles;
        this.playerName = playerName;
        this.score = score;
        this.playerId = playerId;
    }

    public int getLeftTiles() {
        return leftTiles;
    }

    public String getPlayerName() {
        return playerName != null ? playerName : "";
    }

    public char[][] getBoard() {
        return board;
    }

    public short getPlayerId() {
        return playerId;
    }

    public float getScore() {
        return score;
    }
}
