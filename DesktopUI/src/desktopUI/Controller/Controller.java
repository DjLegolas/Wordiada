
package desktopUI.Controller;

import desktopUI.scoreDetail.ScoreDetailController;
import javafx.beans.property.SimpleStringProperty;
import engine.GameEngine;
import engine.Player;
import engine.exceptions.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import desktopUI.GameManager.GameManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
