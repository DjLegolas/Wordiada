package desktopUI.Board;

import desktopUI.Tile.SingleLetterController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.io.IOException;
import java.util.*;

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
        loadBoard();
    }

    // get + set

    public HashMap<Button, SingleLetterController> getButtonsList() {
        return buttonsList;
    }

    public void setBoardValues(char [][] boardView){
        for(int i = 0 ; i < size; i++){
            for(int j = 0; j < size; j ++){
                char sign = boardView[i][j];
                Node button = getNodeByRowColumnIndex(i+1,j+1,boardGridPane);
                String setSign = String.format("%c",sign);
                buttonsList.get(button).setLetter(setSign);

            }
        }

    }
    public void updateNodeToTile(javafx.scene.control.Button button){
        SingleLetterController slc = new SingleLetterController(button);
        slc.initialize();
        buttonsList.put(button,slc);
    }


    public void loadBoard(){
        for (short row = 1; row < size+1; row++) {
            for (short col = 1; col < size+1; col++) {
                //TODO: change from button to tile with letter fdata from game engine

                Button tile = new Button();
                tile.setPrefSize(Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);
                tile.setMinSize(50,50);
                tile.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                            if (tile.getStyle().equals("-fx-border-color: blue")) {
                                tile.setStyle("");
                            }
                            else {
                                tile.setStyle("-fx-border-color: blue");
                            }
                        }
                    }
                });
                this.updateNodeToTile(tile);
                boardGridPane.add(tile, col, row );
            }
        }
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


    // helper gridpane func
    public Node getNodeByRowColumnIndex ( int row,  int column, GridPane gridPane) {
        Node result = null;

        java.util.List<Node> childrens = new ArrayList<>();
        childrens =  gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}
