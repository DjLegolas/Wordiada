package desktopUI.GameManager;

import desktopUI.Controller.Controller;
import desktopUI.scoreDetail.ScoreDetailController;
import desktopUI.scoreDetail.WordDetails;
import desktopUI.userInfo.UserInfoController;
import engine.GameEngine;
import engine.Player;
import engine.exceptions.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class GameManager {

    private GameEngine gameEngine = new GameEngine();

    public GameManager(){}

    public String getDataPlayers(){
        StringBuilder dataPlayers = new StringBuilder();
        for(Player p : gameEngine.getPlayers()){
            dataPlayers.append(p.toString() + " " + p.getScore() + "\n");
        }
        return dataPlayers.toString();
    }

    public void getDataPlayers(Pane node){
        URL infoFXML = getClass().getResource("../userInfo/UserInfo.fxml");
        for(Player player : gameEngine.getPlayers()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(infoFXML);
            HBox root = null;
            try {
                root = loader.load();
            }
            catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Unable to load user nfo").show();
            }
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

    public void loadXML(File xmlFile, Controller controller){
        new Thread(() -> {
        try {
            gameEngine.loadXml(xmlFile.getPath());
            Platform.runLater(() -> controller.initGame());
        }
        catch(WrongPathException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "Wrong path exception!!! ass hollllllle").show());
        }
        catch(DictionaryNotFoundException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "There is not dictinary file!").show());
        }
        catch(BoardSizeException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "invalid board size").show());
        }
        catch(NotXmlFileException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "This is not an XML file! u mother fucker").show());
        }
        catch(DuplicateLetterException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "duplicate fucking letter!!!").show());
        }
        catch(NotValidXmlFileException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "Not valid xmk file").show());
        }
        catch(WinTypeException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "win cheat thing").show());
        }
        catch(NotEnoughLettersException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "Not fucjing enouth letters u IDIOT").show());
        }
        catch (NumberOfPlayersException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "num on players.. needed" + e.getMinPlayers() + " to " +
                    e.getMinPlayers() + "... have " + e.getActualNumOfPlayers()).show());
        }
        catch (DuplicatePlayerIdException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "duplicated player fuckin id " + e.getDuplicateId()).show());
        }}).start();
    }

    public void startGame() {
        gameEngine.startGame();
    }

    public void showWords(ScoreDetailController scoreDetailController) {
        scoreDetailController.setIsCapitalist(gameEngine.isWordScore());
        long totalWords = 0;
        for (Map.Entry<String, Pair<Integer, Integer>> entry: gameEngine.getPlayerWords().entrySet()) {
            scoreDetailController.getWords().add(new WordDetails(entry.getKey(), entry.getValue().getKey(), entry.getValue().getValue()));
            totalWords += entry.getValue().getKey();
        }
        scoreDetailController.setWordsAmount(totalWords);
    }

    public void getDiceValue(SimpleIntegerProperty diceValue) {
        diceValue.set(gameEngine.getDiceValue());
    }

    public void exitGame() {
        if (gameEngine.isStarted()) {

        }
    }
}
