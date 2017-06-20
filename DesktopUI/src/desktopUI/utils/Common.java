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


    //TODO: change all the code areas i used this func without call it
    public static void showMessage(String title, String message, Alert.AlertType type){
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }
}
