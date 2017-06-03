package desktopUI.Board;

import desktopUI.Tile.SingleLetterController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class Board {

    private GridPane boardGridPane;
    private short size;
    private HashMap<Button, SingleLetterController> buttonsList;

    public Board(short size, GridPane boardGridPane) {

        //Ido's code:
        /*
        this.size = size;
        this.boardGridPane = boardGridPane;
        this.boardGridPane.setAlignment(Pos.CENTER);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.boardGridPane.add(createTile(), col, row);
            }
        }*/
        this.size = size;
        this.boardGridPane = boardGridPane;
        buttonsList = new HashMap<>();
    }

    // get + set

    public HashMap<Button, SingleLetterController> getButtonsList() {
        return buttonsList;
    }

    public void updateNodeToTile(javafx.scene.control.Button button){
        SingleLetterController slc = new SingleLetterController(button);
        slc.initialize();
        buttonsList.put(button,slc);
    }

    public Node createTile() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("desktopUI/Tile/Tile.fxml"));
            Button singleLetterTile = loader.load();

            SingleLetterController singleLetterController = loader.getController();
            buttonsList.put(singleLetterTile, singleLetterController);

            return singleLetterTile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
