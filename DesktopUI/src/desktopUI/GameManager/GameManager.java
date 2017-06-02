package desktopUI.GameManager;

import engine.GameEngine;
import engine.Player;
import engine.exceptions.*;
import javafx.scene.control.Alert;

import java.io.File;

public class GameManager {

    private GameEngine gameEngine = new GameEngine();

    public GameManager(){}
    public String getDataPlayers(){
        StringBuilder dataPlayers = new StringBuilder();
        for(Player p : gameEngine.getPlayers()){
            dataPlayers.append(p.toString() + "\n");
        }
        return dataPlayers.toString();
    }

    public void loadXML(File xmlFile){

        try {
            gameEngine.loadXml(xmlFile.getPath());
        }
        catch(WrongPathException e) {
            new Alert(Alert.AlertType.ERROR, "Wrong path exception!!! ass hollllllle").show();
        }
        catch(DictionaryNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "There is not dictinary file!").show();
        }
        catch(BoardSizeException e) {
            new Alert(Alert.AlertType.ERROR, "invalid board size").show();
        }
        catch(NotXmlFileException e) {
            new Alert(Alert.AlertType.ERROR, "This is not an XML file! u mother fucker").show();
        }
        catch(DuplicateLetterException e) {
            new Alert(Alert.AlertType.ERROR, "duplicate fucking letter!!!").show();
        }
        catch(NotValidXmlFileException e) {
            new Alert(Alert.AlertType.ERROR, "Not valid xmk file").show();
        }
        catch(WinTypeException e) {
            new Alert(Alert.AlertType.ERROR, "win cheat thing").show();
        }
        catch(NotEnoughLettersException e) {
            new Alert(Alert.AlertType.ERROR, "Not fucjing enouth letters u IDIOT").show();
        }

        try{
            gameEngine.startGame();
        }
        catch(NumberOfPlayersException e)
        {
            System.out.println("Fail in init players");

        }


    }

}
