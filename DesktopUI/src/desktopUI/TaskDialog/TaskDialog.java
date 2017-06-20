package desktopUI.TaskDialog;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.concurrent.Callable;

public class TaskDialog {
    @FXML private Label messageLabel;
    @FXML private ProgressBar progressBar;

    private Callable<Void> retire;

    public TaskDialog() {
    }

    public StringProperty textProperty() {
        return messageLabel.textProperty();
    }

    public DoubleProperty progressProperty() {
        return progressBar.progressProperty();
    }

    public void setRetire(Callable<Void> retire) {
        this.retire = retire;
    }

    @FXML
    public void onCancel() {
        try {
            retire.call();
        }
        catch (Exception e) {

        }
    }
}
