package desktopUI;

import engine.GameEngine;
import engine.exceptions.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {
    protected GameEngine gameEngine = new GameEngine();
    //private Controller controller = new Controller(gameEngine);

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("desktopUI.fxml");
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();
        Controller controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Please choose one of the options");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        //controller.loadXmlFile(primaryStage);
        //   controller.showMenu(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
      //  Application.launch(Main.class, args);
           // Application.launch(args);
    }



}
