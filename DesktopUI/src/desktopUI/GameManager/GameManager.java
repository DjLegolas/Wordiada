package desktopUI.GameManager;

import desktopUI.Controller.Controller;
import desktopUI.TaskDialog.TaskDialog;
import desktopUI.Tile.SingleLetterController;
import desktopUI.scoreDetail.ScoreDetailController;
import desktopUI.scoreDetail.WordDetails;
import desktopUI.userInfo.UserInfoController;
import desktopUI.utils.Common;
import desktopUI.utils.HelperFuncs;
import desktopUI.utils.HelperFuncs.*;
import engine.ComputerTask;
import engine.GameEngine;
import engine.GameEngine.CaptureTheMoment;
import engine.Player;
import engine.exceptions.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class GameManager {
    private int currentDiceValue;
    private int tryNumber = 1;
    private short currentPlayerId;
    private boolean isFishMod;

    private GameEngine gameEngine = new GameEngine();
    private Controller controller;

    public GameManager(Controller controller) {
        this.controller = controller;
    }

    public Boolean getIsFishMod() {
        return isFishMod;
    }

    public int getCurrentDiceValue() {
        return currentDiceValue;
    }

    public void setCurrentDiceValue(int currentDiceValue) {
        this.currentDiceValue = currentDiceValue;
    }

    public Map<Short, UserInfoController> getDataPlayers(Pane node){
        Map<Short, UserInfoController> controllersMap = new HashMap<>();
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
            controllersMap.put(player.getId(), userInfoController);
            node.getChildren().add(root);
        }
        return controllersMap;
    }

    public String getInitInfoGame(){
        //TODO: add the total amount of kupa tiles in the information!
        return String.format(
                "Size Board: %d x %d\n" + "\n" +
                "Score type: %s \n" + "\n" +
                "Gold Fish Mod: %s\n" + "\n" +
                "Top ten rare words: \n %s" + "\n" +
                "Frequency for each letter:\n %s",
                gameEngine.getBoardSize(), gameEngine.getBoardSize(),
                gameEngine.getWinScoreMod(),
                gameEngine.isInGoldFishMod().toString(),
                gameEngine.getTopTenRareWords(),
                gameEngine.getFreqEachLetter());
    }

    public int getMaxTries() {
        return gameEngine.getMaxRetries();
    }

    public GameEngine getGameEngine(){return gameEngine;}

    public void loadXML(File xmlFile){
        new Thread(() -> {
            try {
                gameEngine.loadXml(xmlFile.getPath());
                Platform.runLater(() -> controller.initGame());
            }
            catch(WrongPathException e) {
                Common.showError("Wrong path to Xml file!");
            }
            catch(DictionaryNotFoundException e) {
                Common.showError("There is not dictinary file!");
            }
            catch(BoardSizeException e) {
                Common.showError("Invalid board size!");
            }
            catch(NotXmlFileException e) {
                Common.showError("This is not an XML file! ");
            }
            catch(DuplicateLetterException e) {
                Common.showError("Duplicate letter Error!");
            }
            catch(NotValidXmlFileException e) {
                Common.showError("Not valid Xml file!");
            }
            catch(WinTypeException e) {
                Common.showError("Not entered win score mod!");
            }
            catch(NotEnoughLettersException e) {
                Common.showError("Not enough letters!");
            }
            catch (NumberOfPlayersException e) {
                Common.showError("Number of players needs to be between " + e.getMinPlayers() + " to " +
                            e.getMinPlayers() + ", and you entered "  + e.getActualNumOfPlayers() + "!");
            }
            catch (DuplicatePlayerIdException e) {
                Common.showError("Duplicate Id player Error! " + e.getDuplicateId());
        }}).start();
    }

    public short startGame() {
        gameEngine.startGame();
        currentPlayerId = gameEngine.getCurrentPlayerId();
        isFishMod = gameEngine.getCurrentGameData().getGoldFishMod();
        updateTurnNumber();
        return currentPlayerId;
    }

    public void showWords(int playerId, ScoreDetailController scoreDetailController) {
        scoreDetailController.setIsCapitalist(gameEngine.isWordScore());
        long totalWords = 0;
        Map<String, Pair<Integer, Integer>> words = gameEngine.getPlayerWords(playerId);
        for (Map.Entry<String, Pair<Integer, Integer>> entry: words.entrySet()) {
            scoreDetailController.getWords().add(new WordDetails(entry.getKey(), entry.getValue().getKey(), entry.getValue().getValue()));
            totalWords += entry.getValue().getKey();
        }
        scoreDetailController.setWordsAmount(totalWords);
    }

    public void getDiceValue(SimpleIntegerProperty diceValue) {
        diceValue.set(gameEngine.getDiceValue());
    }

    public void setUnclickableButtons(List<Button> stayClickableButtons, List<Button> allBoardButtons){
        for(Button button : allBoardButtons){
            if(stayClickableButtons.contains(button)){
                button.setDisable(false);
            }
            else if (!button.getText().equals("")) {
                button.setDisable(false);
            }
            else
                button.setDisable(true);
        }
    }

    public void setDefaultStyle(List<Button> buttonsList){
        for(Button button : buttonsList){
            button.setStyle("");
        }
    }

    public void updateBoard(List<int[]> selectedButtonsPoints) {
        boolean enoughTiles;
        try {
            enoughTiles = gameEngine.updateBoard(selectedButtonsPoints);
        } catch (OutOfBoardBoundariesException e) {
            Common.showError("How did you managed to select one tile out of boundaries?");
            return;
        }
        if (enoughTiles) {

            char[][] board = gameEngine.getBoard();
            Platform.runLater(() -> controller.updateBoard(board));
        }
        else {
            Common.showError("You chose too many tiles! you need to choose only " + currentDiceValue + " tiles \n\nTry again.");

        }
    }

    public void retire() {
        if (gameEngine.isStarted()) {
            if (gameEngine.retirePlayer()) {
                short retiredId = currentPlayerId;
                controller.playerRetired(retiredId);
                nextTurn(false);
            } else {
                controller.endOfGameStatus();
            }
        }
    }

    public void exitGame() {
        Platform.exit();
    }

    private void updateTurnNumber() {
        int turn = gameEngine.getStatistics().getNumOfTurns();
        Platform.runLater(() -> controller.getTurnProperty().set(turn));
    }
    private void updateTurnNumber(int turnNum) {
        Platform.runLater(() -> controller.getTurnProperty().set(turnNum));
    }

    private void updatePlayerScore() {
        Map<Short, UserInfoController> userInfoControllerMap = controller.getUserInfoMap();
        List<Player> players = gameEngine.getPlayers();
        for (Player player: players) {
            if (player.getId() == currentPlayerId) {
                Platform.runLater(() -> userInfoControllerMap.get(player.getId()).setScoreProperty(player.getScore()));
                break;
            }
        }
    }

    public String buttonsToStr(List<Button>letters, char [][]signs) {

        // int sizeListButtons = letters.size();
        //  List<Character> word = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        for (Button letter : letters) {
            int sizeIdButton = letter.getId().length();
            String id = letter.getId();
            int col = id.getBytes()[sizeIdButton - 1] - 48 - 1;
            int row = id.getBytes()[sizeIdButton - 2] - 48 - 1;
            char sign = signs[row][col];
            word.append(sign);
        }
        return  word.toString();
    }

    //check if ita a good word and calc score if necessary
    public void checkWord(String word) {
        Alert alert;
        switch (gameEngine.isWordValidWithCoordinates(word, tryNumber, new ArrayList<>(controller.getBoard().getPressedButtonsIndices()))) {
            case CORRECT:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Check Word");
                alert.setContentText("Well done! your word \"" + word + "\" is correct!!!");
                // should no be here
                // removeWordFromBoard();
                alert.setHeaderText(null);
                alert.showAndWait();
                nextTurn(true);
                break;
            case WRONG:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Check Word");
                alert.setContentText("Uhh.. the word \"" + word + "\" is wrong!!!");
                alert.setHeaderText(null);
                alert.showAndWait();
                tryNumber++;
                controller.getTryNumberProperty().set(tryNumber);
                break;
            case TRIES_DEPLETED:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Check Word");
                alert.setContentText("Oops.. you have got No more tries!!");
                alert.setHeaderText(null);
                alert.showAndWait();
                nextTurn(false);
                break;
            case CHARS_NOT_PRESENT:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Check Word");
                alert.setContentText("Oops.. you enter INVALID chars!");
                alert.setHeaderText(null);
                alert.showAndWait();
                tryNumber++;
                controller.getTryNumberProperty().set(tryNumber);
                break;
            case WRONG_CANT_RETRY:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Check Word");
                alert.setContentText("Uhh..This word is Wrong.\nThat was your last try..  !");
                alert.setHeaderText(null);
                alert.showAndWait();
                nextTurn(false);
                break;
        }
    }

    private void switchUser() {
        short newId = gameEngine.getCurrentPlayerId();
        short prevId;
        if (newId != currentPlayerId) {
            prevId = currentPlayerId;
            Platform.runLater(() -> controller.selectPlayer(prevId, newId));
            currentPlayerId = newId;
        }
        else {
            controller.checkComputer();
        }
    }

    public boolean isPlayerComputer() {
        return gameEngine.isPlayerComputer();
    }

    public void runComputer() {
        FXMLLoader loader = new FXMLLoader();
        URL infoFXML = getClass().getResource("/desktopUI/TaskDialog/TaskDialog.fxml");
        loader.setLocation(infoFXML);
        AnchorPane root;
        try {
            root = loader.load();
        }
        catch (IOException e) {
            Common.showError("Unable to show progress window");
            return;
        }


        Stage stage = new Stage();
        stage.setTitle("Computer Run - Wordiada");
        stage.setScene(new Scene(root));

        ComputerTask computerTask = new ComputerTask(gameEngine.getCurrentPlayer(), gameEngine);
        TaskDialog taskDialog = loader.getController();
        taskDialog.textProperty().bind(computerTask.messageProperty());
        taskDialog.progressProperty().bind(computerTask.progressProperty());
        /*taskDialog.setRetire(() -> {
                retire();
                return null;
            });*/
        computerTask.setOnSucceeded(workerStateEvent -> {
            Platform.runLater(() -> {
                stage.hide();
                stage.close();
                controller.resetBoard(gameEngine.getBoard());
            });
            nextTurn(computerTask.getValue());
        });
        new Thread(computerTask).start();
        stage.show();
    }

    private void nextTurn(boolean clearButtons) {
        updatePlayerScore();
        switchUser();
        updateTurnNumber();
        tryNumber = 1;
        Platform.runLater(() -> controller.resetTurn(clearButtons));
    }

    /*
    // should not be here
    public void removeWordFromBoard(){
        List<Point> lettersToRemove = controller.getBoard().fromSingleLetterToPoint();
        gameEngine.getBoardObject().removeLettersFromBoard(lettersToRemove);
        //update turn info
        // gameEngine.saveTheTurn(controller.getBoard().getPressedButtons());
    }
    */

    public void reset() {
        new Thread(() -> {
            gameEngine.reset();
            tryNumber = 1;
            Platform.runLater(() -> {
                controller.initGame();
                controller.playTurn();
            });
        }).start();
    }

    private void setTurnValues(CaptureTheMoment turnValues){
        if (turnValues != null) {
            int turnNum = turnValues.getTurnNumber();
            updateTurnNumber(turnNum);
            updatePlayerScore();
            switchUser();
            Platform.runLater(() -> controller.savedBoardUpdate(turnValues.getBoard(), turnValues.getSelectedBoardButtons()));
        }
        else {
            Platform.runLater(() -> controller.getNextButton().setDisable(true));
        }
    }

    public void prevSaveData() {
        setTurnValues(gameEngine.prevSaveData());
        if (!gameEngine.havePrevSave()) {
            controller.getPrevButton().setDisable(true);
        }
    }
    public void nextSaveData() {
        setTurnValues(gameEngine.nextSaveData());
        if (!gameEngine.haveNextSave()) {
            controller.getNextButton().setDisable(true);
        }
    }
}

