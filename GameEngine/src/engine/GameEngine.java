package  engine;

import java.lang.String;
import engine.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {

    // private GameInformation info;
    private Player player;
    private List<GameDataFromXml> gdfx = new ArrayList<>();
    private GameDataFromXml currentGameData;
    private boolean isGameStarted = false;
    private int diceValue;
    private Board board;

    //Cto'r
    GameEngine(String pathToXml){

       //TODO: check with ido if there is a need for ctor
    }

    public void loadXml(String pathToXml) {
        GameDataFromXml gd = new GameDataFromXml();
        gd.initializingDataFromXml(pathToXml);
        //check validation:
        try{
            gd.isAllLettersApperOne();
            gd.isDictionaryInRightPos(gd.getDictFileName(), pathToXml);
            gd.isValidBoardSize(gd.getBoardSize());
            gd.isValidXml(pathToXml);
        }
        catch(InvalidInputException e){
            //TODO: ask ido
        }
        catch(WrongPathException e){
            //TODO: ask ido
        }
        catch(NotXmlFileException e){
            //TODO: ask ido
        }
        gdfx.add(gd);
    }


    public boolean isXmlLoaded() {
        return !gdfx.isEmpty();
    }

    public boolean isStarted() {
        return isGameStarted;
    }

    public void startGame() {
        currentGameData =  gdfx.get(0);
        isGameStarted = true;
    }

    public Object getStat() {
        return new Integer(1);
    }

    public int getDiceValue() {
        Random random = new Random();
        diceValue = random.nextInt(currentGameData.getNumOfCubeWigs() - 2) + 2;
        return diceValue;
    }

    public void updateBoard(List<int[]> points) {
    }

    public Object getBoard() {
        return new Integer(1);
    }

    public int getMaxRetries() {
        return 1;
    }

    public boolean isWordValid(String word, int tries) {
        if (tries <= currentGameData.getNumOfTries()) {
            if (true) { //TODO: check in dictionary
                //TODO: update user
                return true;
            }
            return false;
        }
        return false; //TODO: throw NumOfRetriesException
    }

    public Statistics getStatistics() {
        return new Statistics(null, System.currentTimeMillis(), 0, 0);
    }
}
