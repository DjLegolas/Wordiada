package engine;


import java.util.List;

public class Statistic {

    private List<Player> players ;
    private int numOfTurns;
    private int leftBoxTiles;
    private float time; //TODO: calculate
    private GameDataFromXml gameData;


    Statistic(List <Player> input_Player){
        players.addAll(input_Player) ;
        numOfTurns = 0;
        //leftBoxTiles = gameData.getLeftBoxTiles();
    }

}