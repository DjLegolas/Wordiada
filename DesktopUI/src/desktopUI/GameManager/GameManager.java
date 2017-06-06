package desktopUI.GameManager;

import desktopUI.Controller.Controller;
import desktopUI.scoreDetail.ScoreDetailController;
import desktopUI.scoreDetail.WordDetails;
import desktopUI.userInfo.UserInfoController;
import desktopUI.utils.Common;
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
    private int currentDiceValue;



    //TODO: add num of tries and all of this....

    private GameEngine gameEngine = new GameEngine();

    public GameManager(){}

    public int getCurrentDiceValue() {
        return currentDiceValue;
    }

    public void setCurrentDiceValue(int currentDiceValue) {
        this.currentDiceValue = currentDiceValue;
    }

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
                Common.showError("Unable to load user nfo");
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

    public void loadXML(File xmlFile, Controller controller){
        new Thread(() -> {
            try {
                gameEngine.loadXml(xmlFile.getPath());
                Platform.runLater(() -> controller.initGame());
            }
            catch(WrongPathException e) {
                Common.showError("Wrong path exception!!! ass hollllllle");
            }
            catch(DictionaryNotFoundException e) {
                Common.showError("There is not dictinary file!");
            }
            catch(BoardSizeException e) {
                Common.showError("invalid board size");
            }
            catch(NotXmlFileException e) {
                Common.showError("This is not an XML file! u mother fucker");
            }
            catch(DuplicateLetterException e) {
                Common.showError("duplicate fucking letter!!!");
            }
            catch(NotValidXmlFileException e) {
                Common.showError("Not valid xmk file");
            }
            catch(WinTypeException e) {
                Common.showError("win cheat thing");
            }
            catch(NotEnoughLettersException e) {
                Common.showError("Not fucjing enouth letters u IDIOT");
            }
            catch (NumberOfPlayersException e) {
                Common.showError("num on players.. needed" + e.getMinPlayers() + " to " +
                            e.getMinPlayers() + "... have " + e.getActualNumOfPlayers());
            }
            catch (DuplicatePlayerIdException e) {
                Common.showError("duplicated player fuckin id " + e.getDuplicateId());
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
