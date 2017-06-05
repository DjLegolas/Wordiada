package desktopUI.scoreDetail;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ScoreDetailController {
    @FXML private Label wordCountLabel;
    @FXML private TableView<WordDetails> wordsTableView;
    @FXML private TableColumn scoreTableColumn;
    private ObservableList<WordDetails> data;
    private SimpleLongProperty wordsCountProperty;
    private SimpleBooleanProperty isCapitalistProperty;

    public ScoreDetailController() {
        data = FXCollections.observableArrayList();
        wordsCountProperty = new SimpleLongProperty();
        isCapitalistProperty = new SimpleBooleanProperty(false);
    }

    public void initialize() {
        wordCountLabel.textProperty().bind(Bindings.format("%,d", wordsCountProperty));
        scoreTableColumn.visibleProperty().bind(isCapitalistProperty);
        wordsTableView.setItems(data);
        wordsTableView.setPlaceholder(new Label("The user don't have words"));
    }

    public void setWordsAmount(long amount) {
        wordsCountProperty.set(amount);
    }

    public ObservableList<WordDetails> getWords() {
        return data;
    }

    public void setIsCapitalist(boolean isCapitalist) {
        isCapitalistProperty.set(isCapitalist);
    }
}
