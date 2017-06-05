package desktopUI.Board;

import com.sun.security.auth.SolarisNumericUserPrincipal;
import com.sun.xml.internal.ws.api.pipe.Engine;
import desktopUI.Tile.SingleLetterController;
import engine.GameDataFromXml;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import engine.GameDataFromXml.DataLetter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.io.IOException;
import java.util.*;
import java.util.List;

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

    // probably will not need this func--->
    public void setAllBoardValues(char [][] boardView, List<DataLetter> dataLetterList){
        for(int i = 0 ; i < size; i++){
            for(int j = 0; j < size; j ++){
                char sign = boardView[i][j];
                Node button = getNodeByRowColumnIndex(i+1,j+1,boardGridPane);
                DataLetter dataLetter = findDataLetterBySign(sign, dataLetterList);
                if(dataLetter == null) {
                    System.out.println("Error in getting the letters' information from xml properly !");
                }
                else {
                    String setSign = String.format("%c (%d)", sign, dataLetter.getLetter().getScore());
                    buttonsList.get(button).setLetter(setSign);
                }

            }
        }

    }

    public void setPressedButtonsValues(char[][] boardView, List<Button> pressedButtons, List<DataLetter> dataLetterList){
        for(int i = 0 ; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char sign = boardView[i][j];
                Node button = getNodeByRowColumnIndex(i+1,j+1,boardGridPane);
                DataLetter dataLetter = findDataLetterBySign(sign, dataLetterList);
                if(isButtonExist(pressedButtons,(Button)button)){
                    String setSign = String.format("%c (%d)", sign, dataLetter.getLetter().getScore());
                    buttonsList.get(button).setLetter(setSign);
                }
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
                Button tile = new Button();
                tile.setId(String.format("tile%d%d",row,col));
                tile.setPrefSize(Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);
                tile.setMinSize(70,70);
                tile.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                            if (tile.getStyle().equals("-fx-border-color: blue")) {
                                tile.setStyle("");
                            }
                            else {
                                tile.setStyle("-fx-border-color: blue");
                                tile.setBackground(new Background(new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, null)));
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

    //TODO: put those 3 funcs in a static class which support helper funcs without spesific subject
    //1.   helper gridpane func
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

    //2. helper func for findinng a dataLetter type in a list:

    public DataLetter findDataLetterBySign(char sign,List<DataLetter>dataLetterList){

        for(DataLetter dataLetter : dataLetterList){
            if((dataLetter.getLetter().getSign().get(0).toCharArray())[0] == sign)
                return dataLetter;
            }
        return null;
    }


    //3. helper func to fins if item exist in a list
    public boolean isButtonExist(List<Button> buttons, Button buttonToFind){
        for(Button button : buttons){
            if(buttonToFind == button)
                return true;
        }
        return false;
    }
}
