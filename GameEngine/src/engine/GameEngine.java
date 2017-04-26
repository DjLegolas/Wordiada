package  engine;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.Character;

import java.lang.String;
import engine.exceptions.*;


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
        // TODO
    }






}
