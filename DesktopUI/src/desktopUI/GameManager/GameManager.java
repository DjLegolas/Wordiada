package desktopUI.GameManager;

import desktopUI.Controller.Controller;
import desktopUI.Tile.SingleLetterController;
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
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
    private int currentDiceValue;
    private int tryNumber = 1;
    private short currentPlayerId;


    //TODO: add num of tries and all of this....

    private GameEngine gameEngine = new GameEngine();
    private Controller controller;

    public GameManager(Controller controller) {
        this.controller = controller;
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
        return String.format("Size Board: %d x %d\n" +"\n"+
                        "Score type: %s \n" +"\n"+
                        "Gold Fish Mod: %s\n" + "\n"+
                        "Top ten rare words: \n %s" +"\n"+
                        "Frequency for each letter:\n %s",
                        gameEngine.getBoardSize(), gameEngine.getBoardSize(),
                        gameEngine.getWinScoreMod(),gameEngine.isInGoldFishMod().toString(),
                        gameEngine.getTopTenRareWords(),gameEngine.getFreqEachLetter());

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

    public short startGame() {
        gameEngine.startGame();
        currentPlayerId = gameEngine.getCurrentPlayerId();
        return currentPlayerId;
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
            Common.showError("You selected too many tiles. you need only " + currentDiceValue);
        }
    }

    public void exitGame() {
        if (gameEngine.isStarted()) {
            //TODO: guss what....
        }
    }

    private void updateTurnNumber() {
        int turn = gameEngine.getStatistics().getNumOfTurns();
        Platform.runLater(() -> controller.getTurnProperty().set(turn));
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

    public String buttonsToStr(List<Button>letters, Map<Button, SingleLetterController>infoAboutLetters,char [][]signs) {

        int sizeListButtons = letters.size();
        //  List<Character> word = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < sizeListButtons; i++) {
            int sizeIdButton = letters.get(i).getId().length();
            String id = letters.get(i).getId();
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
        switch (gameEngine.isWordValid(word, tryNumber)) {
            case CORRECT:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulations! your word \"" + word + "\" is correct!!!");
                alert.setContentText(word);
                alert.setHeaderText(null);
                alert.show();
                nextTurn(true);
                break;
            case WRONG:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Boooooz! u r a failure! \"" + word + "\" is wrong!!!");
                alert.setContentText(word);
                alert.setHeaderText(null);
                alert.show();
                tryNumber++;
                controller.getTryNumberProperty().set(tryNumber);
                break;
            case TRIES_DEPLETED:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("nooooob! u got no more retries!");
                alert.setContentText(word);
                alert.setHeaderText(null);
                alert.show();
                nextTurn(false);
                break;
            case CHARS_NOT_PRESENT:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("OMG!! how did u managed to enter invalid char?");
                alert.setContentText(word);
                alert.setHeaderText(null);
                alert.show();
                tryNumber++;
                controller.getTryNumberProperty().set(tryNumber);
                break;
            case WRONG_CANT_RETRY:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Boooooz! u r a failure! \"" + word + "\" is wrong!!!\n" +
                        "nooooob! u got no more retries!");
                alert.setContentText(word);
                alert.setHeaderText(null);
                alert.show();
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
    }

    private void nextTurn(boolean clearButtons) {
        updatePlayerScore();
        switchUser();
        updateTurnNumber();
        tryNumber = 1;
        Platform.runLater(() -> controller.resetTurn(clearButtons));
    }
}

