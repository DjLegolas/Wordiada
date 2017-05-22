package desktopUI;

import engine.GameEngine;
import engine.exceptions.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;

public class Main extends Application {
    private GameEngine gameEngine = new GameEngine();

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
        Parent root = FXMLLoader.load(getClass().getResource("../resources/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose xml file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All files", "*.*"),
                                                 new FileChooser.ExtensionFilter("XML", "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(primaryStage);
        
        try {
            gameEngine.loadXml(xmlFile.getPath());
        }
        catch(WrongPathException e) {
            new Alert(Alert.AlertType.ERROR, "Wrong path exception!!! ass hollllllle").show();
        }
        catch(DictionaryNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "There is not dictinary file!").show();
        }
        catch(BoardSizeException e) {
            new Alert(Alert.AlertType.ERROR, "invalid board size").show();
        }
        catch(NotXmlFileException e) {
            new Alert(Alert.AlertType.ERROR, "This is not an XML file! u mother fucker").show();
        }
        catch(DuplicateLetterException e) {
            new Alert(Alert.AlertType.ERROR, "duplicate fucking letter!!!").show();
        }
        catch(NotValidXmlFileException e) {
            new Alert(Alert.AlertType.ERROR, "Not valid xmk file").show();
        }
        catch(WinTypeException e) {
            new Alert(Alert.AlertType.ERROR, "win cheat thing").show();
        }
        catch(NotEnoughLettersException e) {
            new Alert(Alert.AlertType.ERROR, "Not fucjing enouth letters u IDIOT").show();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }



}
