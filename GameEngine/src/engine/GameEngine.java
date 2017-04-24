package  engine;



import engine.jabx.schema.generated.Letter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.lang.Character;
import java.lang.String;


public class GameEngine {

    // private GameInformation info;
    private Player player;
    private GameDataFromXml gdfx;


    GameEngine(String pathToXml){
        gdfx.initializingDataFromXml(pathToXml);
    }

    public void loadXml(String pathToXml) {
        // TODO
    }


    public boolean isValidXml(String pathToXml) {
        String extension = pathToXml.substring(pathToXml.lastIndexOf(".") + 1, pathToXml.length());
        if ((extension != "xml") || (extension != ".xml"))
            return false;
        return true;
    }

    // call this func after calling the one above
    public boolean isDictionaryInRightPos(String pathToDictFile, String pathToXml){
        pathToXml = pathToXml.substring(0, pathToXml.length() - 4); // minus 4 for ".xml"
        while(!pathToXml.endsWith("\\")){
            pathToXml = pathToXml.substring(0,pathToXml.length() - 1);
        }
        if(pathToDictFile == pathToXml + gdfx.getDictFileName() + ".txt" )
            return true;
        return false;
    }

    public boolean isValidBoardSize(byte size){
        if((size >= 5) && (size <= 50))
            return true;
        return false;
    }

    public boolean isAllLettersApperOne(){
        boolean isMoreThanOnce = false;
        for(int i = 0; i < gdfx.getLetters().size(); i++){
            Letter l = gdfx.getLetters().get(i);
            String c = gdfx.getLetters().get(i).getSign().get(0);
            gdfx.getLetters().remove(i);
            isMoreThanOnce = gdfx.getLetters().contains(c);
            gdfx.getLetters().add(i,l);
            //appears more than once
            if(isMoreThanOnce)
                return false;
        }
        return true;
    }





}
