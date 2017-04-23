package  engine;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GameEngine {

    private GameDataFromXml data;
    // private GameInformation info;
    private Player player;




    public void loadXml(String pathToXml) {
        // TODO
    }


    public boolean isValidXml(String pathToXml) {
        String extension = pathToXml.substring(pathToXml.lastIndexOf(".") + 1, pathToXml.length());
        if ((extension != "xml") || (extension != ".xml"))
            return false;
        return true;
    }
    public boolean isDictionaryInRightPos(){
        return true;
    }


}
