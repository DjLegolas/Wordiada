package  engine;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameEngine {

    private GameDataFromXml data;
    // private GameInformation info;
    private Player player;




    public void loadXml(String pathToXml) {
    }


    public boolean isValidXml(String pathToXml) {
        String extension = pathToXml.substring(pathToXml.lastIndexOf(".") + 1, pathToXml.length());
        if ((extension != "xml") || (extension != ".xml"))
            return false;
        return true;
    }
    private boolean isDictionaryInRightPos(String dic) {
        return true;
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
