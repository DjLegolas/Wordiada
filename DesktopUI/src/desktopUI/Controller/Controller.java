
package desktopUI.Controller;

import desktopUI.Board.Board;
import desktopUI.userInfo.UserInfoController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    private GameManager gameManager = new GameManager();
    private Board board;
    private List<Button> pressedButtons = new ArrayList<>();
    private Map<Short, UserInfoController> userInfoControllerMap;

    @FXML private  GridPane boardPane;
    @FXML private VBox buttonsVBox;
    @FXML private Button loadXmlButton;
    @FXML private Button startButton;
    @FXML private Button diceButton;
    @FXML private Button moveButton;
    @FXML private Button exitButton;
    @FXML private Label player1;
    @FXML private Label turnNumber;
    @FXML private ScrollPane boardScrollPane;
    @FXML private Label notAvailable;
    @FXML private  Label initInfoGame;
    @FXML private  Label titleInfoGame;
    @FXML private  Label titlePlayerData;
    @FXML private VBox playerVBox;
    @FXML private Button buildWord;
    @FXML private Button checkWord;


    private SimpleStringProperty selectedPlayerData;
    private SimpleIntegerProperty selectedTurnNumber;
    private SimpleStringProperty selectedInitInfoGame;
    private SimpleStringProperty selectedTitleInfoGame;
    private SimpleStringProperty selectedTitlePlayerData;
    private SimpleIntegerProperty diceValueProperty;

    public Controller(){
        selectedPlayerData = new SimpleStringProperty();
        selectedPlayerData.set("Player");
        selectedTurnNumber = new SimpleIntegerProperty();
        selectedTurnNumber.set(0);
        selectedInitInfoGame = new SimpleStringProperty();
        selectedTitleInfoGame = new SimpleStringProperty();
        selectedTitlePlayerData = new SimpleStringProperty();
        diceValueProperty = new SimpleIntegerProperty();

   }

    @FXML
    public void initialize() {
         player1.textProperty().bind(selectedPlayerData);
         turnNumber.textProperty().bind(Bindings.format("%,d", selectedTurnNumber));
         initInfoGame.textProperty().bind(selectedInitInfoGame);
         titleInfoGame.textProperty().bind(selectedTitleInfoGame);
         titlePlayerData.textProperty().bind(selectedTitlePlayerData);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void reinitialize() {
        board = null;
        userInfoControllerMap = null;
        boardPane.getChildren().clear();
        selectedPlayerData.set("Player");
        selectedTurnNumber.set(0);
        selectedInitInfoGame.set("");
        moveButton.setDisable(true);
        diceButton.setDisable(true);
        playerVBox.getChildren().clear();
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
        gameManager.loadXML(xmlFile, this);
    }

    public void initGame() {
        reinitialize();
        notAvailable.setText("");
        selectedPlayerData.set(gameManager.getDataPlayers());
        userInfoControllerMap = gameManager.getDataPlayers(playerVBox);
        selectedInitInfoGame.set(gameManager.getInitInfoGame());
        //init board
        board = new Board(gameManager.getGameEngine().getBoardSize(),boardPane);
        startButton.setDisable(false);
        selectedTitleInfoGame.set("Information about the Game:");
        selectedTitlePlayerData.set("Information about the Players:");
    }

    // helper func
        public Node getNodeByRowColumnIndex ( int row,  int column, GridPane gridPane) {
        Node result = null;

        List <Node> childrens = new ArrayList<>();
        childrens =  gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    @FXML
    public void startGame() {
        gameManager.startGame();
        board.setIsClickable(true);
    }

    @FXML public void diceThrow(){
        Alert dieMessage = new Alert(Alert.AlertType.INFORMATION,"Please throw the die");
        dieMessage.setTitle("Play Turn");
        dieMessage.setHeaderText("Throwing die...");
        dieMessage.show();
    }


    //TODO: change name of func to relevant one and decide what to do here!!!!!
    @FXML public void playTurn() {
        int diceValue;

        // adding the pressed tile to the list:
        loadXmlButton.setDisable(true);
        moveButton.setDisable(false);
        diceButton.setDisable(false);
        gameManager.startGame();
      
        //show instructions
        //TODO: create a button for showing these instructions if necessary (put the on action in this func and not globaly)
        //TODO : put unclickable board till the throw dice button
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setContentText("The game is started!\npress on Throw Die button to see the value of your die, and then you" +
                             "need to choose tiles on the board as many as the die's value.\nOnce you choose press on the " +
                             "Make a Move button which will shows you the hidden letters." +
                             "\n\n Have Fun!!!");

        alert.setHeaderText(null);
        alert.show();
    }

    @FXML public void makeMove() {

        moveButton.setDisable(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setContentText(" Now you can watch are the hidden letters you chose to open!\n\nPlease press on the Build A Word button to continue the game.");

        alert.setHeaderText(null);
        alert.show();
        //TODO: find a way to watch the word the user chose - mabye label to show  him the word and offer him to change if neccesary
        buildWord.setDisable(false);
        moveButton.setDisable(true);
        board.setPressedButtonsValues(gameManager.getGameEngine().getBoardObject().getBoardWithAllSignsShown(),board.getPressedButtons(),gameManager.getGameEngine().getCurrentGameData().getKupa());
        gameManager.setUnclickableButtons(board.getPressedButtons(),board.getBoardButtonList());
        gameManager.setDefaultStyle(board.getPressedButtons());

        //change the target of the button to be the check word issue
        moveButton.setOnMouseClicked((MouseEvent event) -> {
            checkWord();
        });



        //TODO: CHECK IF ITS A GOOD WORD
        //the word that the user chose
        /*
        String wordToCheck = chosenTiles.toString();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chosen Word");
        alert.setContentText("Are you sure about this word?");
        alert.setHeaderText(null);
        alert.show();
        alert.setContentText("The word you chose is:" + wordToCheck);
        */

    }
    @FXML public void checkWord(){
        //TODO:  check why its noy working when putting the result in variable word
       String word = gameManager.buttonsToStr(board.getPressedButtons(),board.getButtonsList(),gameManager.getGameEngine().getBoardObject().getBoardWithAllSignsShown());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Building A Word");
        alert.setContentText(word);
        alert.setHeaderText(null);
        alert.show();
    }
    @FXML public void buildWord(){
        checkWord.setDisable(false);
        board.getPressedButtons().clear();
        moveButton.setDisable(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setContentText("Now that you chose the letters, all you need is to create a word that combines them. It can combines all letters or part of them." +
                             "\nOnce you have a word in your mind, press on the letters by the order in which they appear in the word. (If you accidentally pressed the wrong order, that's fine, just press again, so the letters will be covered by a blue background).\n When you are done, press on the Make a Move button.\n\nGood Luch!!!");

        alert.setHeaderText(null);
        alert.show();


    }

    @FXML
    //TODO: make unclick the throw a dice button but keep the info about the value somewhere so the player can watch anytime
    public void throwDie() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dice Throw - Wordiada");
        alert.setContentText("Throwing dice...");
        alert.setHeaderText(null);
        alert.show();
        gameManager.getDiceValue(diceValueProperty);
        board.setIsClickable(true);
        diceButton.setDisable(true);
        alert.setContentText("Dice value is " + diceValueProperty.get());
        gameManager.setCurrentDiceValue(diceValueProperty.get());
    }

    @FXML
    public void exitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameManager.exitGame();
        }
    }
}
