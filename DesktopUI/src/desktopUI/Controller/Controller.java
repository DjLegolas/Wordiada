
package desktopUI.Controller;

import desktopUI.Board.Board;
import javafx.beans.property.SimpleIntegerProperty;
import engine.GameDataFromXml;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import desktopUI.GameManager.GameManager;
import engine.GameDataFromXml.DataLetter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {


    private Stage primaryStage;
    private GameManager gameManager = new GameManager();
    private Board board;
    private List<Button> pressedButtons = new ArrayList<>();

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

    private SimpleStringProperty selectedPlayerData;
    private SimpleStringProperty selectedTurnNumber;    //TODO: convert to integer
    private SimpleStringProperty selectedInitInfoGame;
    private SimpleStringProperty selectedTitleInfoGame;
    private SimpleStringProperty selectedTitlePlayerData;
    private SimpleIntegerProperty diceValueProperty;

    public Controller(){
        selectedPlayerData = new SimpleStringProperty();
        selectedPlayerData.set("Player");
        selectedTurnNumber = new SimpleStringProperty();
        selectedTurnNumber.set("0");
        selectedInitInfoGame = new SimpleStringProperty();
        selectedTitleInfoGame = new SimpleStringProperty();
        selectedTitlePlayerData = new SimpleStringProperty();
        diceValueProperty = new SimpleIntegerProperty();

   }

    @FXML
    public void initialize() {
         player1.textProperty().bind(selectedPlayerData);
         turnNumber.textProperty().bind(selectedTurnNumber);
         initInfoGame.textProperty().bind(selectedInitInfoGame);
         titleInfoGame.textProperty().bind(selectedTitleInfoGame);
         titlePlayerData.textProperty().bind(selectedTitlePlayerData);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void reinitialize() {
        board = null;
        boardPane.getChildren().clear();
        selectedPlayerData.set("Player");
        selectedTurnNumber.set("0");
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
        gameManager.getDataPlayers(playerVBox);
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
        board.setIsClickable(true);
    }

    @FXML public void makeMove() {

        //TODO: find a way to watch the word the user chose - mabye label to show  him the word and offer him to change if neccesary


        List<Button> chosenTiles = new ArrayList<>();
        for(int i = 0; i < pressedButtons.size(); i++ ){
            chosenTiles.add(i, pressedButtons.get(i));
        }

        board.setPressedButtonsValues(gameManager.getGameEngine().getBoardObject().getBoardWithAllSignsShown(),board.getPressedButtons(),gameManager.getGameEngine().getCurrentGameData().getKupa());
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

    @FXML
    public void throwDie() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dice Throw - Wordiada");
        alert.setContentText("Throwing dice...");
        alert.setHeaderText(null);
        alert.show();
        gameManager.getDiceValue(diceValueProperty);
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
