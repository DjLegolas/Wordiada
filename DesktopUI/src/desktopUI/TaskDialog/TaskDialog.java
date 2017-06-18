package desktopUI.TaskDialog;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class TaskDialog {
    @FXML private Label messageLabel;
    @FXML private ProgressBar progressBar;

    public TaskDialog() {
    }

    public StringProperty textProperty() {
        return messageLabel.textProperty();
    }

    public DoubleProperty progressProperty() {
        return progressBar.progressProperty();
    }

    @FXML
    public void onCancel() {

    }
}
