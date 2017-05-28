
package desktopUI;

import engine.GameEngine;
import engine.exceptions.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class Controller {

    private GameEngine gameEngine;
    private Stage primaryStage;

    @FXML private VBox buttonsVBox;
    @FXML private Button loadXmlButton;
    @FXML private Button startButton;
    @FXML private Button moveButton;
    @FXML private Button exitButton;

    @FXML private ScrollPane boardScrollPane;


    public Controller(){
        this.gameEngine = new GameEngine();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMenu(Stage primaryStage){
        final Button b1 = new Button("Load game from xml");
        final Button b2 = new Button("Start game");
        final Button b3 = new Button("Show status");
        final Button b4 = new Button( "Make a move");
        final Button b5 = new Button( "Show statistics");
        final Button b6 = new Button("Exit game");

        b1.setOnAction(e -> loadXmlFile());
    }

    @FXML
    public void loadXmlFile(){
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
}
