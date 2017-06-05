package desktopUI.GameManager;

import desktopUI.scoreDetail.ScoreDetailController;
import desktopUI.scoreDetail.WordDetails;
import desktopUI.userInfo.UserInfoController;
import engine.GameEngine;
import engine.Player;
import engine.exceptions.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.File;
import java.net.URL;
import java.util.Map;

public class GameManager {



    //TODO: add num of tries and all of this....

    private GameEngine gameEngine = new GameEngine();

    public GameManager(){}

    public String getDataPlayers(){
        StringBuilder dataPlayers = new StringBuilder();
        for(Player p : gameEngine.getPlayers()){
            dataPlayers.append(p.toString() + " " + p.getScore() + "\n");
        }
        return dataPlayers.toString();
    }

    public void getDataPlayers(Pane node) throws Exception{
        URL infoFXML = getClass().getResource("../userInfo/UserInfo.fxml");
        for(Player player : gameEngine.getPlayers()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(infoFXML);
            HBox root = loader.load();
            UserInfoController userInfoController = loader.getController();
            userInfoController.setGameManager(this);
            userInfoController.setIdProperty(player.getId());
            userInfoController.setNameProperty(player.getName());
            userInfoController.setPlayerTypeProperty(player.getType());
            userInfoController.setScoreProperty(player.getScore());
            node.getChildren().add(root);
        }
    }

    public String getInitInfoGame(){
        //TODO: add the total amount of kupa tiles in the information!
        return String.format("Size Board: %d x %d\n" +"\n"+
                        "Score type: %s \n" +"\n"+
                        "Gold Fish Mod: %s\n" + "\n"+
                        "Top ten rare words: \n %s" +"\n"+
                        "Frequency for each letter:\n %s",
                        gameEngine.getBoardSize(), gameEngine.getBoardSize(),
                        gameEngine.getWinScoreMod(),gameEngine.isInGoldFishMod().toString(),
                        gameEngine.getTopTenRareWords(),gameEngine.getFreqEachLetter());

    }

    public GameEngine getGameEngine(){return gameEngine;}

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

    public void showWords(ScoreDetailController scoreDetailController) {
        scoreDetailController.setIsCapitalist(gameEngine.isWordScore());
        for (Map.Entry<String, Pair<Integer, Integer>> entry: gameEngine.getPlayerWords().entrySet()) {
            scoreDetailController.getWords().add(new WordDetails(entry.getKey(), entry.getValue().getKey(), entry.getValue().getValue()));
        }
    }

    public void getDiceValue(SimpleIntegerProperty diceValue) {
        diceValue.set(gameEngine.getDiceValue());
    }
}
