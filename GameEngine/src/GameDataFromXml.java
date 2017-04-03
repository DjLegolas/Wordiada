import java.util.HashMap;
import java.util.Map;

public class GameDataFromXml {

    Map <Character,Float> frequencyLetter = new HashMap <Character, Float>();
    Map <String,Float> frequencyWord = new HashMap <String, Float>();
    private int boardSize;
    private int numOfCubeWigs;
    private int numOfTries;
    private int totalTiles;
    private int leftBoxTiles;

    //TODO: initializing by the xml

    public class Board {

        private GameDataFromXml boardData;
        private int tilesInBoard;
    }


    public int getLeftBoxTiles(){
        return this.leftBoxTiles;
    }


    //TODO: all illiegele issues + xml loading + txt file in dictionary library

}