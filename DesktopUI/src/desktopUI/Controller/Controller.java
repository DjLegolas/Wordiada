
package desktopUI.Controller;

import javafx.beans.property.SimpleStringProperty;
import engine.GameEngine;
import engine.Player;
import engine.exceptions.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import desktopUI.GameManager.GameManager;

import java.io.File;

public class Controller {


    private Stage primaryStage;
    private GameManager gameManager = new GameManager();

    @FXML private VBox buttonsVBox;
    @FXML private Button loadXmlButton;
    @FXML private Button startButton;
    @FXML private Button moveButton;
    @FXML private Button exitButton;
    @FXML private Label player1;
    @FXML private ScrollPane boardScrollPane;
    @FXML private Label NotAviable;

    private SimpleStringProperty selectedPlayerData;

    public Controller(){
        selectedPlayerData = new SimpleStringProperty();
   }

    @FXML
    private void initialize() {
         player1.textProperty().bind(selectedPlayerData);
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

    }
}
