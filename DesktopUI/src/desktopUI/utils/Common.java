package desktopUI.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Common {
    public static void showError(String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(error);
            alert.showAndWait();
        });
    }
}
