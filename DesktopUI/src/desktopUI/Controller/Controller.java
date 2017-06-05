
package desktopUI.Controller;

import desktopUI.Board.Board;
import javafx.beans.property.SimpleIntegerProperty;
import engine.GameDataFromXml;
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
import engine.GameDataFromXml.DataLetter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private SimpleStringProperty selectedTurnNumber;
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



    // was removed to board class:

    /*
    @FXML
    public void loadBoard(short sizeBoard) {

        //build cols + rows

        board = new Board(sizeBoard,boardPane);
        for (short row = 0; row < sizeBoard; row++) {
            for (short col = 0; col < sizeBoard; col++) {
                //TODO: change from button to tile with letter fdata from game engine

                Button tile = new Button();
                GridPane.setMargin(tile, new Insets(1, 1, 1, 1));
                board.updateNodeToTile(tile);
                boardPane.add(tile, col, row);
            }
        }
    }*/



    @FXML
    public void loadXmlFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose xml file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        fileChooser.setInitialDirectory(new File("C:\\Users\\noy\\Desktop\\לימודים\\IdeaProjects\\Wordiada"));
        File xmlFile = fileChooser.showOpenDialog(primaryStage);
        gameManager.loadXML(xmlFile, this);
    }

    public void initGame() {
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
    }

    @FXML public void diceThrow(){
        Alert dieMessage = new Alert(Alert.AlertType.INFORMATION,"Please throw the die");
        dieMessage.setTitle("Play Turn");
        dieMessage.setHeaderText("Throwing die...");
        dieMessage.show();
    }

    @FXML public void playTurn() {
        int diceValue;

        if (moveButton.isPressed())
            diceThrow();
        // adding the pressed tile to the list:
        moveButton.setDisable(false);
        diceButton.setDisable(false);

        for (Node button : boardPane.getChildren()) {

            button.setOnMousePressed(new javafx.event.EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    pressedButtons.add((Button) button);
                    //if the player finished to choose letters
                    if (pressedButtons.size() == gameManager.getGameEngine().getDiceValue()) ;
                    {
                        board.setPressedButtonsValues(gameManager.getGameEngine().getBoardObject().getBoardWithAllSignsShown(), pressedButtons, gameManager.getGameEngine().getCurrentGameData().getKupa());
                    }
                }
            });



        }
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
    }

    @FXML
    public void exitGame() {
        gameManager.exitGame();
    }
}
