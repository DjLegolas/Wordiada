package engine;


import java.util.List;

public class Statistics {

    private List<Player> players ;
    private int numOfTurns;
    private int leftBoxTiles;
    private long time; //TODO: calculate
    private GameDataFromXml gameData;

    Statistics(List <Player> input_Player, long secondsPassed, int TurnsPlayed, int cardsLeft){
        players.addAll(input_Player) ;
        time = secondsPassed;
        numOfTurns = TurnsPlayed;
        leftBoxTiles = cardsLeft;
    }

    public long getTime() {
        return time;
    }

    public int getNumOfTurns() {
        return numOfTurns;
    }

    public int getLeftBoxTiles() {
        return leftBoxTiles;
    }

}