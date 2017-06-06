package desktopUI.Tile;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SingleLetterController {

    @FXML private Button letterButton;
    private SimpleStringProperty letterProperty;




    public SingleLetterController(Button button) {
        letterProperty = new SimpleStringProperty();
        letterButton = button;

    }

    @FXML
    public void initialize() {
        letterButton.textProperty().bind(letterProperty);
        letterProperty.set("");
        //scoreLabel.textProperty().bind(Bindings.format("(%d)", scoreProperty));
    }

    public void setLetter(String letter) {
        letterProperty.set(letter);
    }

    public SimpleStringProperty getLetterProperty() {
        return letterProperty;
    }

    public Button getLetterButton() {
        return letterButton;
    }


}
