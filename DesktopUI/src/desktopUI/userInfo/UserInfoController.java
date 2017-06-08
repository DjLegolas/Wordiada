package desktopUI.userInfo;

import com.sun.scenario.effect.impl.state.HVSeparableKernel;
import desktopUI.GameManager.GameManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class UserInfoController {
    @FXML private HBox userInfoHBox;
    @FXML private Label nameLabel;
    @FXML private Label idLabel;
    @FXML private Label playerTypeLabel;
    @FXML private Label scoreLabel;
    @FXML private Button detailsButton;

    private SimpleStringProperty nameProperty;
    private SimpleStringProperty playerTypeProperty;
    private SimpleIntegerProperty idProperty;
    private SimpleFloatProperty scoreProperty;
    private SimpleStringProperty styleProperty;

    private GameManager gameManager;

    public UserInfoController() {
        nameProperty = new SimpleStringProperty();
        playerTypeProperty = new SimpleStringProperty();
        idProperty = new SimpleIntegerProperty();
        scoreProperty = new SimpleFloatProperty();
        styleProperty = new SimpleStringProperty();
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @FXML
    public void initialize() {
        nameLabel.textProperty().bind(nameProperty);
        idLabel.textProperty().bind(Bindings.format("%d", idProperty));
        playerTypeLabel.textProperty().bind(Bindings.format("(%s)", playerTypeProperty));
        scoreLabel.textProperty().bind(Bindings.format("%.1f", scoreProperty));
        nameLabel.styleProperty().bind(styleProperty);
        idLabel.styleProperty().bind(styleProperty);
        playerTypeLabel.styleProperty().bind(styleProperty);
        scoreLabel.styleProperty().bind(styleProperty);
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public void setPlayerTypeProperty(String playerTypeProperty) {
        this.playerTypeProperty.set(playerTypeProperty);
    }

    public void setIdProperty(int idProperty) {
        this.idProperty.set(idProperty);
    }

    public void setScoreProperty(float scoreProperty) {
        this.scoreProperty.set(scoreProperty);
    }

    public void setStyleProperty(String styleProperty) {
        this.styleProperty.set(styleProperty);
    }

    public void disableDetailsButton(boolean isDisable) {
        detailsButton.setDisable(isDisable);
    }

    @FXML
    public void showWords() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("../scoreDetail/ScoreDetail.fxml");
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        gameManager.showWords(loader.getController());

        Stage stage = new Stage();
        stage.setTitle("Player Words - Wordiada");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setStrikeThrough() {
        userInfoHBox.getStylesheets().addAll(getClass().getResource("Strikethrough.css").toExternalForm());
    }
}
