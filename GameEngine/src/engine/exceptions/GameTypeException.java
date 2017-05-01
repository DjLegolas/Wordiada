package engine.exceptions;

public class GameTypeException extends Exception {
    private String gameType;

    public GameTypeException(String gameType) {
        super();
        this.gameType = gameType;
    }

    public String getGameType() {
        return gameType;
    }
}
