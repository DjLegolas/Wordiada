import java.util.HashMap;
import java.util.Map;

public class GameDataFromXml {

    Map <String,String> frequencyLetter = new HashMap <String, String>();
    Map <String,String> frequencyWord = new HashMap <String, String>();
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
