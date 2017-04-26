package  engine;

import java.lang.String;
import engine.exceptions.*;
import java.util.List;

public class GameEngine {

    // private GameInformation info;
    private Player player;
    private GameDataFromXml gdfx;

    //Cto'r
    GameEngine(String pathToXml){

        gdfx.initializingDataFromXml(pathToXml);

        //check validation:
        try{
            gdfx.isAllLettersApperOne();
            gdfx.isDictionaryInRightPos(gdfx.getDictFileName(), pathToXml);
            gdfx.isValidBoardSize(gdfx.getBoardSize());
            gdfx.isValidXml(pathToXml);
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

    }

    public void loadXml(String pathToXml) {
    }

    public boolean isXmlLoaded() {
        return true;
    }

    public boolean isStarted() {
        return false;
    }

    public void startGame() {
    }

    public Object getStat() {
        return new Integer(1);
    }

    public int getDiceValue() {
        return 0;
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
        return true;
    }

    public Statistics getStatistics() {
        return new Statistics(null, System.currentTimeMillis(), 0, 0);
    }
}
