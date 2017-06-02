package desktopUI.Board;

import desktopUI.Tile.SingleLetterController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;

public class Board {
    private GridPane boardGridPane;
    private short size;
    private HashMap<Node, SingleLetterController> nodeToTileController;

    public Board(short size, GridPane boardGridPane) {
        this.size = size;
        this.boardGridPane = boardGridPane;
        this.boardGridPane.setAlignment(Pos.CENTER);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.boardGridPane.add(createTile(), col, row);
            }
        }
    }

    private Node createTile() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("desktopUI/Tile/Tile.fxml"));
            Node singleLetterTile = loader.load();

            SingleLetterController singleLetterController = loader.getController();
            nodeToTileController.put(singleLetterTile, singleLetterController);

            return singleLetterTile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
