package desktopUI.Board;

import desktopUI.Tile.SingleLetterController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import engine.GameDataFromXml.DataLetter;

import java.awt.*;
import desktopUI.utils.Common;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;

public class Board {

    private GridPane boardGridPane;
    private Callable<Boolean> wordBuilder;
    private short size;
    private Map<Button, SingleLetterController> buttonsMap;
    private List<Button> pressedButtons = new ArrayList<>();
    private boolean buildWord = false;

    public Board(short size, GridPane boardGridPane) {

        this.size = size;
        this.boardGridPane = boardGridPane;
        buttonsMap = new HashMap<>();
        loadBoard();
    }

    // get + set


    public List<Button> getPressedButtons() {
        return pressedButtons;
    }

    public List<int[]> getPressedButtonsIndices() {
        List<int[]> indexList = new ArrayList<>();
        for (Button button: pressedButtons) {
            int[] point = {GridPane.getRowIndex(button), GridPane.getColumnIndex(button)};
            indexList.add(point);
        }
        return indexList;
    }

    public List<Button> getButtonsFromIndices(List<int[]> buttonsIndices) {
        List<Button> list = new ArrayList<>();
        for (int[] index: buttonsIndices) {
            Button button = (Button)getNodeByRowColumnIndex(index[0], index[1], boardGridPane);
            list.add(button);
        }
        return list;
    }

    public Map<Button, SingleLetterController> getButtonsMap() {
        return buttonsMap;
    }

    // probably will not need this func--->
    /*public void setAllBoardValues(char [][] boardView, List<DataLetter> dataLetterList){
        for(int i = 0 ; i < size; i++){
            for(int j = 0; j < size; j ++){
                char sign = boardView[i][j];
                Button button = getNodeByRowColumnIndex(i+1,j+1,boardGridPane);
                DataLetter dataLetter = findDataLetterBySign(sign, dataLetterList);
                if(dataLetter == null) {
                    System.out.println("Error in getting the letters' information from xml properly !");
                }
                else {
                    String setSign = String.format("%c (%d)", sign, dataLetter.getLetter().getScore());
                    buttonsMap.get(button).setLetter(setSign);
                }
            }
        }
    }*/

    public void setPressedButtonsValues(char[][] boardView, List<Button> pressedButtons, List<DataLetter> dataLetterList){
        for(int i = 0 ; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char sign = boardView[i][j];
                Node button = getNodeByRowColumnIndex(i+1,j+1,boardGridPane);
                DataLetter dataLetter = findDataLetterBySign(sign, dataLetterList);
                if(isButtonExist(pressedButtons,(Button)button)){
                    String setSign = String.format("%c (%d)", sign, dataLetter.getLetter().getScore());
                    buttonsMap.get(button).setLetter(setSign);
                }
            }
        }
    }

    public void setPressedButtonsValues(char[][] boardView, List<DataLetter> dataLetterList){
        for (Button button: pressedButtons) {
            int row = GridPane.getRowIndex(button) - 1;
            int col = GridPane.getColumnIndex(button) - 1;
            char sign = boardView[row][col];
            DataLetter dataLetter = findDataLetterBySign(sign, dataLetterList);
            String setSign = String.format("%c (%d)", sign, dataLetter.getLetter().getScore());
            buttonsMap.get(button).setLetter(setSign);
        }
    }

    public void updateFromSave(char[][] boardView, List<Button> pressedButtons, List<DataLetter> dataLetterList) {
        for(int i = 0 ; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char sign = boardView[i][j];
                Button button = (Button) getNodeByRowColumnIndex(i+1,j+1,boardGridPane);
                DataLetter dataLetter = findDataLetterBySign(sign, dataLetterList);
                if (sign == ' ') {
                    buttonsMap.get(button).setLetter("");
                }
                else {
                    String setSign = String.format("%c (%d)", sign, dataLetter.getLetter().getScore());
                    buttonsMap.get(button).setLetter(setSign);
                }
                if (pressedButtons.contains(button)) {
                    button.setStyle("-fx-border-color: blue;" +
                            "-fx-background-color: aqua");
                }
                else {
                    button.setStyle("");
                }
            }
        }
    }

    public void resetPressStyle() {
        for (Button button: pressedButtons) {
            button.setStyle("");
        }
    }

    public void updateNodeToTile(javafx.scene.control.Button button){
        SingleLetterController slc = new SingleLetterController(button);
        slc.initialize();
        buttonsMap.put(button,slc);
    }

    private void loadBoard(){
        for (short row = 1; row < size+1; row++) {
            for (short col = 1; col < size+1; col++) {
                Button tile = new Button();
                tile.setId(String.format("tile%d%d",row,col));
                tile.setPrefSize(Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);
                tile.setMinSize(70,70);
                GridPane.setMargin(tile, new Insets(1, 1, 1, 1));
                //tile.disableProperty().bind(isClickable.not());
                tile.setDisable(true);
                tile.setOnMouseClicked((MouseEvent event) -> {
                        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                            boolean allClickable = areAllButtonsClickable();
                            if(buttonsMap.get(tile).getLetterProperty().getValue()!="" && allClickable){
                                Common.showError("you Cannot open an already open tile!\n\nTry again!");
                            }
                            else {
                                if (pressedButtons.contains(tile) && tile.getStyle().equals("-fx-border-color: blue;" +
                                        "-fx-background-color: aqua")) {
                                    //if (tile.getStyle().equals("-fx-border-color: blue;" + "-fx-background-color: aqua")){
                                    tile.setStyle("");
                                    pressedButtons.remove(tile);
                                    if (buildWord) {
                                        try {
                                            wordBuilder.call();
                                        } catch (Exception e) {
                                        }
                                    }
                                } else {
                                    tile.setStyle("-fx-border-color: blue;" +
                                            "-fx-background-color: aqua");
                                    pressedButtons.add(tile);
                                    if (buildWord) {
                                        try {
                                            wordBuilder.call();
                                        } catch (Exception e) {
                                        }
                                    }
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
            buttonsMap.put(singleLetterTile, singleLetterController);

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

        java.util.List<Node> children;
        children = gridPane.getChildren();

        for (Node node : children) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
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


    //3. helper func to find if item exists in a list
    public boolean isButtonExist(List<Button> buttons, Button buttonToFind){
        for(Button button : buttons){
            if(buttonToFind == button)
                return true;
        }
        return false;
    }

    public List<Button> getBoardButtonList(){
        List<Button> retList = new ArrayList<>();
        retList.addAll(buttonsMap.keySet());
        return retList;
    }

    public void setAllDisable(boolean disable) {
        for (Button button: buttonsMap.keySet()) {
            button.setDisable(disable);
        }
    }

    public void resetPressedButtons() {
        for (Button button: pressedButtons) {
            buttonsMap.get(button).setLetter("");
        }
    }

    public void resetAllButtons(){
        for (Button button: buttonsMap.keySet()) {
            buttonsMap.get(button).setLetter("");
            button.setStyle("");
            button.setDisable(true);
        }
    }

    public void buildWord(boolean buildWord, Callable<Boolean> wordBuilder) {
        this.buildWord = buildWord;
        this.wordBuilder = wordBuilder;
    }

    public void removeAllBoardButtons(){
        for(SingleLetterController buttonlLetter : buttonsMap.values())
            buttonlLetter.setLetter("");
    }
    public List<Point> fromSingleLetterToPoint(){
        List<Point> points = new ArrayList<>();

        for(Button button: pressedButtons){
            int sizeId = button.getId().length();
            int col = button.getId().charAt(sizeId-1) - 48;
            int row = button.getId().charAt(sizeId-2) - 48;
            Point pointToAdd = new Point(col, row);
            points.add(pointToAdd);
        }
        return  points;
    }

    public boolean areAllButtonsClickable(){
        for(Button button : buttonsMap.keySet()){
            if(button.isDisable())
                return false;
        }
        return true;
    }

    public boolean areAllTilesShown(){
        for(SingleLetterController singleLetter : buttonsMap.values()){
            if(singleLetter.getLetterProperty().getValue().equals(""))
                return false;
        }
        return  true;
    }
}
