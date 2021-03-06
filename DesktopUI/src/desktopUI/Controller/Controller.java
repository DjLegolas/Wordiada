
package desktopUI.Controller;

import desktopUI.Board.Board;
import desktopUI.userInfo.UserInfoController;
import desktopUI.utils.Common;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import desktopUI.GameManager.GameManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

 public class Controller {


    private Stage primaryStage;
    private GameManager gameManager;
    private Board board;
    private Map<Short, UserInfoController> userInfoControllerMap;

    @FXML private  GridPane boardPane;
    @FXML private VBox buttonsVBox;
    @FXML private Button loadXmlButton;
    @FXML private Button startButton;
    @FXML private Button diceButton;
    @FXML private Button moveButton;
    @FXML private Button retireButton;
    @FXML private Button exitButton;
    @FXML private Button helpButton;
    @FXML private Label turnNumberLabel;
    @FXML private ScrollPane boardScrollPane;
    @FXML private Label initInfoGame;
    @FXML private Label titleInfoGame;
    @FXML private Label titlePlayerData;
    @FXML private VBox playerVBox;
    @FXML private Button checkWord;
    @FXML private Label buildWordLabel;
    @FXML private Label tryNumberLabel;
    @FXML private Label totalTriesLabel;
    @FXML private Label diceValueLabel;
    @FXML private Button newGameButton;
    @FXML private Button playAgainButton;
    @FXML private Button nextButton;
    @FXML private Button prevButton;

    private SimpleIntegerProperty selectedTurnNumber;
    private SimpleStringProperty selectedInitInfoGame;
    private SimpleStringProperty selectedTitleInfoGame;
    private SimpleStringProperty selectedTitlePlayerData;
    private SimpleIntegerProperty diceValueProperty;
    private SimpleStringProperty wordBuildProperty;
    private SimpleIntegerProperty tryNumberProperty;
    private SimpleIntegerProperty totalTriesProperty;

    public Controller(){
        selectedTurnNumber = new SimpleIntegerProperty();
        selectedTurnNumber.set(0);
        selectedInitInfoGame = new SimpleStringProperty();
        selectedTitleInfoGame = new SimpleStringProperty();
        selectedTitlePlayerData = new SimpleStringProperty();
        diceValueProperty = new SimpleIntegerProperty();
        wordBuildProperty = new SimpleStringProperty();
        tryNumberProperty = new SimpleIntegerProperty();
        totalTriesProperty = new SimpleIntegerProperty();
        gameManager= new GameManager(this);
   }

    @FXML
    public void initialize() {
        turnNumberLabel.textProperty().bind(Bindings.format("%,d", selectedTurnNumber));
        initInfoGame.textProperty().bind(selectedInitInfoGame);
        titleInfoGame.textProperty().bind(selectedTitleInfoGame);
        titlePlayerData.textProperty().bind(selectedTitlePlayerData);
        buildWordLabel.textProperty().bind(wordBuildProperty);
        totalTriesLabel.textProperty().bind(Bindings.format("%,d", totalTriesProperty));
        tryNumberLabel.textProperty().bind(Bindings.format("%,d", tryNumberProperty));
        diceValueLabel.textProperty().bind(Bindings.format("%,d", diceValueProperty));
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public SimpleIntegerProperty getTurnProperty() {
        return selectedTurnNumber;
    }

    public Button getLoadXmlButton() {
        return loadXmlButton;
    }

    public Button getMoveButton() {
        return moveButton;
    }

    public Button getCheckWord() {
        return checkWord;
    }

    public Button getRetireButton() {
        return retireButton;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getDiceButton() {
        return diceButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Button getPrevButton() {
        return prevButton;
    }

    public SimpleStringProperty getWordBuildProperty() {
        return wordBuildProperty;
    }

    public Button getPlayAgainButton() {
        return playAgainButton;
    }

    public Button getNewGameButton() {
        return newGameButton;
    }

    public Map<Short, UserInfoController> getUserInfoMap() {
        return userInfoControllerMap;
    }

    public SimpleIntegerProperty getTryNumberProperty() {
        return tryNumberProperty;
    }

    // init the score of all users to 0
    private void initScore() {
        if (userInfoControllerMap != null) {
            for (UserInfoController userInfoController : userInfoControllerMap.values()) {
                userInfoController.setScoreProperty(0);
            }
        }
    }

    // reset the gama ui
    private void reinitialize() {
        initScore();
        board = null;
        userInfoControllerMap = null;
        boardPane.getChildren().clear();
        selectedTurnNumber.set(0);
        selectedInitInfoGame.set("");
        selectedTurnNumber.set(0);
        moveButton.setDisable(true);
        diceButton.setDisable(true);
        playerVBox.getChildren().clear();
        retireButton.setDisable(true);
    }

    // init turn
    public void resetTurn(boolean clearButtons) {
        wordBuildProperty.set("");
        diceButton.setDisable(false);
        moveButton.setDisable(true);
//        buildWord.setDisable(true);
        checkWord.setDisable(true);
        board.resetPressStyle();
        if (clearButtons) {
            board.resetPressedButtons();
        }
        board.getPressedButtons().clear();
        board.setAllDisable(true);
        board.buildWord(false, null);
        tryNumberProperty.set(0);
        diceValueProperty.set(0);
        //TODO: check if works
        if(gameManager.getIsFishMod()){
            board.removeAllBoardButtons();
        }
    }

    public void resetBoard(char[][] board) {
        this.board.updateFromSave(board, new ArrayList<>(), gameManager.getGameEngine().getCurrentGameData().getKupa());
    }

    @FXML
    public void loadXmlFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose xml file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File noy = new File("C:\\Users\\noy\\Desktop\\לימודים\\IdeaProjects\\Wordiada");
        File ido = new File("D:\\progrmming\\github\\Wordiada");
        if(noy.exists()) fileChooser.setInitialDirectory(noy);
        else if (ido.exists()) fileChooser.setInitialDirectory(ido);
        File xmlFile = fileChooser.showOpenDialog(primaryStage);
        if (xmlFile == null) {
            return;
        }
        gameManager.loadXML(xmlFile);
    }

    // init game
    public void initGame() {
        reinitialize();
        userInfoControllerMap = gameManager.getDataPlayers(playerVBox);
        selectedInitInfoGame.set(gameManager.getInitInfoGame());
        totalTriesProperty.set(gameManager.getMaxTries());
        //init board
        board = new Board(gameManager.getGameEngine().getBoardSize(), boardPane);
        startButton.setDisable(false);
        selectedTitleInfoGame.set("Information about the Game:");
        selectedTitlePlayerData.set("Information about the Players:");
    }

    // helper func
    public Node getNodeByRowColumnIndex ( int row,  int column, GridPane gridPane) {
        Node result = null;

        List <Node> children = gridPane.getChildren();

        for (Node node : children) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    public Board getBoard() {
        return board;
    }

    @FXML public void playTurn() {
        // adding the pressed tile to the list:
        loadXmlButton.setDisable(true);
        startButton.setDisable(true);
        diceButton.setDisable(false);
        retireButton.setDisable(false);
        short id = gameManager.startGame();
        selectPlayer((short)-1, id);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setContentText("The game is started!\npress on Throw Die button to see the value of your die, and then you" +
                             "need to choose tiles on the board as many as the die's value.\nOnce you choose press on the " +
                             "Make a Move button which will shows you the hidden letters." +
                             "\n\n Have Fun!!!");

        alert.setHeaderText(null);
        alert.show();
    }

    public void selectPlayer(short prevId, short newId) {
        UserInfoController userController;
        if (prevId != -1) {
            userController = userInfoControllerMap.get(prevId);
            userController.setStyleProperty("");
            userController.disableDetailsButton(true);
        }
        userController = userInfoControllerMap.get(newId);
        userController.setStyleProperty("-fx-font-weight: bold");
        userController.disableDetailsButton(false);
        checkComputer();
    }

    public void checkComputer() {
        if (!gameManager.getGameEngine().isGameEnded() && gameManager.isPlayerComputer()) {
            gameManager.runComputer();
        }
    }

    private void setAllDisable() {

        loadXmlButton.setDisable(true);
        startButton.setDisable(true);
        diceButton.setDisable(true);
        moveButton.setDisable(true);
        retireButton.setDisable(true);
        exitButton.setDisable(true);
    }

    @FXML public void makeMove() {
        //TODO: check if enough tiles to choose tiles  as dice value
        String notEnoughTilesError = "You need to choose at least one letter!\n\nTry again.";

        if (board.getPressedButtons().isEmpty()) {
            board.setAllDisable(false);
            Common.showError(notEnoughTilesError);
            return;
        }
        tryNumberProperty.set(1);
        gameManager.updateBoard(board.getPressedButtonsIndices());
    }

    public void updateBoard(char[][] board) {
        String outputMessageValidMove = "Now you can watch the hidden letters you chose to open!\n\n" +
                "Try to build a word from those letter by pressing them by the order in which they appear. (If you" +
                " accidentally pressed the wrong order, that's OK, just press again, so the letters will " +
                "be covered by a blue background).\n When you have a word in your mind, press on the Check Word Button to continue.\n\nGood Luck!!!";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setContentText(outputMessageValidMove);
        alert.showAndWait();

        moveButton.setDisable(true);
        checkWord.setDisable(false);
        this.board.setPressedButtonsValues(board, gameManager.getGameEngine().getCurrentGameData().getKupa());
        // this.board.setPressedButtonsValues(board, this.board.getPressedButtons(), gameManager.getGameEngine().getCurrentGameData().getKupa());
        gameManager.setUnclickableButtons(this.board.getPressedButtons(), this.board.getBoardButtonList());
        gameManager.setDefaultStyle(this.board.getPressedButtons());

        this.board.buildWord(true, () -> {
            wordBuildProperty.set(gameManager.buttonsToStr(this.board.getPressedButtons(), gameManager.getGameEngine().getBoard()));
            return Boolean.TRUE;
        });

        this.board.getPressedButtons().clear();
   }

   public void savedBoardUpdate(char[][] board, List<int[]> pressedButtonsIndices) {
       List<Button> buttons = this.board.getButtonsFromIndices(pressedButtonsIndices);
       this.board.updateFromSave(board, buttons, gameManager.getGameEngine().getCurrentGameData().getKupa());
       wordBuildProperty.set(gameManager.buttonsToStr(buttons, board));
   }

    @FXML public void checkWord(){

        moveButton.setDisable(true);

        String word = gameManager.buttonsToStr(board.getPressedButtons(), gameManager.getGameEngine().getBoard());
        gameManager.checkWord(word);

        // num turn +

    }

    @FXML
        public void throwDie() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        boolean allShown = false;
        if(!gameManager.getIsFishMod())
            allShown = board.areAllTilesShown();
        //if the game is over
        if(gameManager.getGameEngine().isGameEnded(allShown)){
            alert.setTitle("Game is Over");
            alert.setContentText("Game's Over...\n Choose one of the buttons options");
            alert.setHeaderText(null);
            alert.showAndWait();
            endOfGameStatus();
        }

        else {
            moveButton.setDisable(false);
            alert.setTitle("Dice Throw - Wordiada");
            alert.setContentText("Throwing dice...");
            alert.setHeaderText(null);
            alert.show();
            gameManager.getDiceValue(diceValueProperty);
            //board.setIsClickable(true);
            board.setAllDisable(false);
            diceButton.setDisable(true);
            alert.setContentText("Dice value is " + diceValueProperty.get());
            gameManager.setCurrentDiceValue(diceValueProperty.get());
        }
    }

    @FXML
    public void exitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameManager.exitGame();
        }
    }

    public void playerRetired(short retiredPlayerId, boolean isRetired) {
        UserInfoController userInfoController = userInfoControllerMap.get(retiredPlayerId);
        userInfoController.setStrikeThrough(isRetired);
    }

    @FXML
    public void retire() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to retire?");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameManager.retire();
        }
    }

    //TODO: fix and put back the call in the button - i changed foe testing
    @FXML
    public void help() {
        // Play turn
        String playTurnHelpTitle = "Instructions";
        String playTurnHelpContent = "The game is started!\npress on Throw Die button to see the value of your die, and then you" +
                "need to choose tiles on the board as many as the die's value.\nOnce you choose press on the " +
                "Make a Move button which will shows you the hidden letters." +
                "\n\n Have Fun!!!";

        //update board
        String updateBoardHelpTitle = "Message";
        String updateBoardHelpContent = "Now you can watch the hidden letters you chose to open!\n\n" +
                "Try to build a word from those letter by pressing them by the order in which they appear. (If you" +
                " accidentally pressed the wrong order, that's OK, just press again, so the letters will " +
                "be covered by a blue background).\n When you have a word in your mind, press on the Check Word Button to continue.\n\nGood Luck!!!";

        String title = null;
        String content = null;

        if (!diceButton.isDisable()) {
            title = playTurnHelpTitle;
            content = playTurnHelpContent;
        }
        else if (!moveButton.isDisable()) {
            title = updateBoardHelpTitle;
            content = updateBoardHelpContent;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    //show the buttons: new game, play again, prev and next
    @FXML
    public void endOfGameStatus(){
        startButton.setDisable(true);
        loadXmlButton.setDisable(true);
        diceButton.setDisable(true);
        moveButton.setDisable(true);
        retireButton.setDisable(true);
        checkWord.setDisable(true);
        board.resetAllButtons();
        initScore();
        next();
        showEndGameControllers(true);
        prevButton.setDisable(true);
    }

    private void showEndGameControllers(boolean show) {
        newGameButton.setVisible(show);
        playAgainButton.setVisible(show);
        prevButton.setVisible(show);
        nextButton.setVisible(show);
    }

    @FXML
    public void loadNewXmlFile() {
        showEndGameControllers(false);
        loadXmlButton.setDisable(false);
        loadXmlFile();
    }

    @FXML public void playAgain(){
        showEndGameControllers(false);
        gameManager.reset();
    }

    @FXML
    public void prev(){
        if (nextButton.isDisable()) {
            nextButton.setDisable(false);
        }
        gameManager.prevSaveData();
    }

     @FXML
     public void next(){
         if (prevButton.isDisable()) {
             prevButton.setDisable(false);
         }
         gameManager.nextSaveData();
     }
}
