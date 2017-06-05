
package desktopUI.Controller;

import desktopUI.scoreDetail.ScoreDetailController;
import desktopUI.Board.Board;
import engine.GameDataFromXml;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import desktopUI.GameManager.GameManager;
import engine.GameDataFromXml.DataLetter;

import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.io.File;
import java.net.URL;
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
    @FXML private Button moveButton;
    @FXML private Button exitButton;
    @FXML private Label player1;
    @FXML private Label turnNumber;
    @FXML private ScrollPane boardScrollPane;
    @FXML private Label NotAviable;
    @FXML private  Label initInfoGame;
    @FXML private  Label titleInfoGame;
    @FXML private  Label titlePlayerData;


    private SimpleStringProperty selectedPlayerData;
    private SimpleStringProperty selectedTurnNumber;
    private SimpleStringProperty selectedInitInfoGame;
    private SimpleStringProperty selectedTitleInfoGame;
    private SimpleStringProperty selectedTitlePlayerData;

    public Controller(){
        selectedPlayerData = new SimpleStringProperty();
        selectedPlayerData.set("Player");
        selectedTurnNumber = new SimpleStringProperty();
        selectedTurnNumber.set("0");
        selectedInitInfoGame = new SimpleStringProperty();
        selectedTitleInfoGame = new SimpleStringProperty();
        selectedTitlePlayerData = new SimpleStringProperty();

   }

    @FXML
    private void initialize() {
         player1.textProperty().bind(selectedPlayerData);
         turnNumber.textProperty().bind(selectedTurnNumber);
         initInfoGame.textProperty().bind(selectedInitInfoGame);
         titleInfoGame.textProperty().bind(selectedTitleInfoGame);
         titlePlayerData.textProperty().bind(selectedTitlePlayerData);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



    @FXML
    public void loadXmlFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose xml file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        fileChooser.setInitialDirectory(new File("C:\\Users\\noy\\Desktop\\לימודים\\IdeaProjects\\Wordiada"));
        File xmlFile = fileChooser.showOpenDialog(primaryStage);
        gameManager.loadXML((xmlFile));
        NotAviable.setText("");
        selectedPlayerData.set(gameManager.getDataPlayers());
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
    @FXML public void diceThrow(){
        Alert dieMessage = new Alert(Alert.AlertType.INFORMATION,"Please throw the die");
        dieMessage.setTitle("Play Turn");
        dieMessage.setHeaderText("Throwing die...");
        dieMessage.show();
    }

    @FXML public void playTurn(){
        int diceValue;

        if (moveButton.isPressed())
            diceThrow();
        // adding the pressed tile to the list:
        moveButton.setDisable(false);

        for( Node button : boardPane.getChildren()) {
            button.setOnMousePressed(new javafx.event.EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    pressedButtons.add((Button) button);
                }
            });

            //if the player finished to choose letters
            if (pressedButtons.size() == gameManager.getGameEngine().getDiceValue()) ;
            {
                board.setPressedButtonsValues(gameManager.getGameEngine().getBoardObject().getBoardWithAllSignsShown(), pressedButtons, gameManager.getGameEngine().getCurrentGameData().getKupa());
            }

        }
    }

    @FXML
    public void showWords() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("../scoreDetail/ScoreDetail.fxml");
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        gameManager.showWords(loader.getController());

        Stage stage = new Stage();
        stage.setTitle("Player Words - Wordiada");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
