package engine.exceptions;

public class NumberOfPlayersException extends Exception{
    private int actualNumOfPlayers;
    private short minPlayers, maxPlayers;

    public NumberOfPlayersException(int numOfPlayers, short minPlayers, short maxPlayers) {
        actualNumOfPlayers = numOfPlayers;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public int getActualNumOfPlayers() {
        return actualNumOfPlayers;
    }

    public short getMinPlayers() {
        return minPlayers;
    }

    public short getMaxPlayers() {
        return maxPlayers;
    }
}
