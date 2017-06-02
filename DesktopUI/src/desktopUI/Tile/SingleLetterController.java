package desktopUI.Tile;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SingleLetterController {
    @FXML private Label letterLabel;
    @FXML private Label scoreLabel;

    private SimpleStringProperty letterProperty;
    private SimpleIntegerProperty scoreProperty;

    public SingleLetterController() {
        letterProperty = new SimpleStringProperty();
        scoreProperty = new SimpleIntegerProperty();
    }

    @FXML
    private void initialize() {
        letterLabel.textProperty().bind(letterProperty);
        scoreLabel.textProperty().bind(Bindings.format("(%d)", scoreProperty));
    }

    public void setLetter(String letter) {
        letterProperty.set(letter);
    }

    public SimpleStringProperty getLetterProperty() {
        return letterProperty;
    }

    public SimpleIntegerProperty getScoreProperty() {
        return scoreProperty;
    }
}
