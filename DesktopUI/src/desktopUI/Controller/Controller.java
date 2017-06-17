
package desktopUI.Controller;

import desktopUI.Board.Board;
import desktopUI.userInfo.UserInfoController;
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
    private GameManager gameManager = new GameManager(this);
    private Board board;
    private List<Button> pressedButtons = new ArrayList<>();
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
    @FXML private  Label initInfoGame;
    @FXML private  Label titleInfoGame;
    @FXML private  Label titlePlayerData;
    @FXML private VBox playerVBox;
    @FXML private Button buildWord; //TODO: what's this?
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

    private void reinitialize() {
        board = null;
        userInfoControllerMap = null;
        boardPane.getChildren().clear();
        selectedTurnNumber.set(0);
        selectedInitInfoGame.set("");
        moveButton.setDisable(true);
        diceButton.setDisable(true);
        playerVBox.getChildren().clear();
        retireButton.setDisable(true);
    }

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

    @FXML
    public void loadXmlFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose xml file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File noy = new File("C:\\Users\\noy\\Desktop\\לימודים\\IdeaProjects\\Wordiada");
        if(noy.exists()) fileChooser.setInitialDirectory(noy);
        File xmlFile = fileChooser.showOpenDialog(primaryStage);
        if (xmlFile == null) {
            return;
        }
        gameManager.loadXML(xmlFile);
    }

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


    @FXML
    public void startGame() {
        gameManager.startGame();
    }

    @FXML public void diceThrow(){
        Alert dieMessage = new Alert(Alert.AlertType.INFORMATION,"Please throw the die");
        dieMessage.setTitle("Play Turn");
        dieMessage.setHeaderText("Throwing die...");
        dieMessage.show();
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
    }

    @FXML public void makeMove() {

        //moveButton.setDisable(false);
        //TODO: check if enough tiles to choose tiles  as dice value
        String outputMessageInValidMove1 = "You need to choose at least one letter!\n\nTry again.";
        String outputMessageInValidMove2 = "You chose too many tiles! you need to choose only " +gameManager.getCurrentDiceValue() + " tiles \n\nTry again.";

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");

        if (board.getPressedButtons().isEmpty()) {
            alert.setContentText(outputMessageInValidMove1);
            alert.show();
            board.setAllDisable(false);
            return;
        }

      //TODO: check the too many tiles error with the accepetion
        if(board.getPressedButtons().size() > gameManager.getCurrentDiceValue()){
            alert.setContentText(outputMessageInValidMove2);
            alert.show();
            board.setAllDisable(false);
            return;
        }
      
        gameManager.updateBoard(board.getPressedButtonsIndices());

        updateBoard(gameManager.getGameEngine().getBoardObject().getBoardWithAllSignsShown());
        board.getPressedButtons().clear();


    }

    public void updateBoard(char[][] board){
        String outputMessageValidMove = "Now you can watch the hidden letters you chose to open!\n\n" +
                "Try to build a word from those letter by pressing them by the order in which they apper. (If you" +
                " accidentally pressed the wrong order, that's OK, just press again, so the letters will " +
                "be covered by a blue background).\n When you have a word in your mind, press on the Check Word Button to continue.\n\nGood Luch!!!";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setContentText(outputMessageValidMove);
        alert.show();

        moveButton.setDisable(true);
        checkWord.setDisable(false);
        this.board.setPressedButtonsValues(board, this.board.getPressedButtons(), gameManager.getGameEngine().getCurrentGameData().getKupa());
        gameManager.setUnclickableButtons(this.board.getPressedButtons(), this.board.getBoardButtonList());
        gameManager.setDefaultStyle(this.board.getPressedButtons());

        this.board.buildWord(true, () -> {
            wordBuildProperty.set(gameManager.buttonsToStr(this.board.getPressedButtons(),this.board.getButtonsMap(),gameManager.getGameEngine().getBoardObject().getBoardWithAllSignsShown()));
            return Boolean.TRUE;
        });
   }

    @FXML public void checkWord(){

        moveButton.setDisable(true);

        String word = gameManager.buttonsToStr(board.getPressedButtons(),board.getButtonsMap(),gameManager.getGameEngine().getBoardObject().getBoardWithAllSignsShown());
        gameManager.checkWord(word);

        // num turn +

    }

    //TODO: remove if we remove the build button - dunno yet

    /*
    @FXML public void buildWord(){
        checkWord.setDisable(false);
        board.getPressedButtons().clear();
        moveButton.setDisable(true);
        buildWord.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setContentText("Now that you chose the letters, all you need is to create a word that combines them. It can combines all letters or part of them." +
                             "\nOnce you have a word in your mind, press on the letters by the order in which they appear in the word. (If you accidentally pressed the wrong order, that's fine, just press again, so the letters will be covered by a blue background).\n When you are done, press on the Make a Move button.\n\nGood Luch!!!");

        alert.setHeaderText(null);
        alert.show();
        board.buildWord(true, () -> {
            wordBuildProperty.set(gameManager.buttonsToStr(board.getPressedButtons(),board.getButtonsMap(),gameManager.getGameEngine().getBoardObject().getBoardWithAllSignsShown()));
            return Boolean.TRUE;
        });
        tryNumberProperty.set(1);
    }*/

    @FXML
    //TODO: make unclick the throw a dice button but keep the info about the value somewhere so the player can watch anytime
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
            alert.show();
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

    public void playerRetired(short retiredPlayerId) {
        UserInfoController userInfoController = userInfoControllerMap.get(retiredPlayerId);
        userInfoController.setStrikeThrough();
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

    @FXML
    public void restart() {

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
                "Try to build a word from those letter by pressing them by the order in which they apper. (If you" +
                " accidentally pressed the wrong order, that's OK, just press again, so the letters will " +
                "be covered by a blue background).\n When you have a word in your mind, press on the Check Word Button to continue.\n\nGood Luch!!!";

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
        newGameButton.setVisible(true);
        playAgainButton.setVisible(true);
        prevButton.setVisible(true);
        nextButton.setVisible(true);
        gameManager.getGameEngine().pointerForTurnData = gameManager.getGameEngine().getTurnData().size();
        board.resetAllButtons();
    }

    //TODO: init all data game to the start
    @FXML public void playAgain(){

    }
    @FXML
    public void prev (){
        gameManager.getGameEngine().pointerForTurnData --;
        gameManager.setTurnValues(gameManager.getGameEngine().getSpesificTurn());

    }
    @FXML
     public void next (){
         gameManager.getGameEngine().pointerForTurnData ++;
        gameManager.setTurnValues(gameManager.getGameEngine().getSpesificTurn());

     }


}
