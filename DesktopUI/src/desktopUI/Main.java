package desktopUI;

import desktopUI.Controller.Controller;
import engine.GameEngine;
import desktopUI.GameManager.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("desktopUI.fxml");
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();
        Controller controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Wordiada");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(root.getMinHeight());
        primaryStage.setMinWidth(root.getMinWidth());
        primaryStage.show();

        //controller.loadXmlFile(primaryStage);
        //   controller.showMenu(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
      //  Application.launch(Main.class, args);
           // Application.launch(args);
    }

    /*
    public void actionPerformed(ActionEvent e) {
        String actionCommand = ((JButton) e.getSource()).getActionCommand();
        if (actionCommand == "Load XML")
            c.showPlayersData();
    }*/



}
