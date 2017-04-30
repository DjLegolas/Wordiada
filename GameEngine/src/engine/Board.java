package engine;

import engine.exceptions.OutOfBoardBoandriesException;
import engine.jaxb.schema.generated.Letter;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class Board {

    public class Point{
        private int x;
        private int y;

        Point(int _x, int _y){
            x = _x;
            y = _y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
    private class Cell{
        boolean isShown;
        String sign;

        Cell(String _sign, boolean _isShown) {
            isShown = _isShown;
            sign = _sign;
        }

    }

    private short size;
    List<GameDataFromXml.DataLetter> kupa = new ArrayList<>();
    Cell [][] board; // for priting
    Map <Letter,List<Point>> initLettrs = new HashMap<>(); //for changes during the game

    public Cell [][] getBoard() {return board;}


    //C'tor
    Board(short _size, List<GameDataFromXml.DataLetter> letters, int totalAmountLetters){

        int x,y;
        Letter toAdd;
        Point p;
        this.board = new Cell[_size][_size];
        size = _size;

        //(x,y) point in the board
        Random xy = new Random();
        //if  equal amount letters to board size
        if(totalAmountLetters == _size * _size)
        {
            for(GameDataFromXml.DataLetter letter : letters){
                toAdd = letter.getLetter();
                List<Point> empty = new ArrayList<>();
                initLettrs.put(toAdd, empty );

                for(int i = 0 ; i < letter.getAmount(); i++){
                    boolean toContinue = false;
                    do {
                        x = xy.nextInt(size);
                        y = xy.nextInt(size);
                        p = new Point(x,y);
                        for (List<Point> listPoints : initLettrs.values()){
                            if (listPoints.contains(p))
                                toContinue = true;
                        }
                    } while (toContinue);
                    initLettrs.get(toAdd).add(p);
                    board[y][x] = new Cell(toAdd.getSign().get(0),false);
                    letter.setAmount(letter.getAmount() - 1);
                }
            }
        }
        //more letters than board size - need for kupa
        else{
            int numOfInsertions = 0;
            while (numOfInsertions <= size*size){
                for(int i =0; i< letters.size(); i++){
                    GameDataFromXml.DataLetter letter = letters.get(i);
                    if(numOfInsertions <= size*size){
                        if(letter.getAmount()>0){
                            toAdd = letter.getLetter();
                            List<Point> empty = new ArrayList<>();
                            initLettrs.put(toAdd, empty );
                            do {
                                x = xy.nextInt(size);
                                y = xy.nextInt(size);
                                p = new Point(x,y);
                            } while (initLettrs.containsValue(p));
                            initLettrs.get(toAdd).add(p);
                            board[y][x] = new Cell(toAdd.getSign().get(0),false);
                            numOfInsertions ++;
                            letter.setAmount(letter.getAmount() - 1);
                        }
                    }
                }
            }

            //build the kupa
            for(GameDataFromXml.DataLetter letter : letters){
                for(int i =0; i< letter.getAmount(); i++){
                    kupa.add(i, letter);
                }
            }
        }
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }


    public void update(List<int[]> points) throws OutOfBoardBoandriesException{

        //check valid point
        for(int[] optionalPoints : points){
            if(optionalPoints[0] > size || optionalPoints[0] < 1 || optionalPoints[1] > size || optionalPoints[1] < 1){
                throw new OutOfBoardBoandriesException();
            }
            else{
                board[optionalPoints[0]][optionalPoints[1]].isShown = true;
            }
        }
    }








    //TODO: remove
/*
    public void printLetterInBoard(char sign){
        System.out.println(String.format("%-30s","h"));
    }*/

/*
    public void printBoard(char[][] board)
    {
        //התקרה
        for(int i =0; i < size; i++){
            System.out.print("-------");
        }
        System.out.println();

        for(int j =0; j < size * 2; j++) {

            //colums
            if(j % 2 == 0) {
                for (int i = 0; i < size + 1; i++) {
                    char ch = 'a';
                    System.out.print("|  " + ch + "   ");
                }
                System.out.println();
                for (int i = 0; i < size + 1; i++) {
                    System.out.print("|      ");
                }
                System.out.println();
            }

            //rows
            else{
                for(int i =0; i < size; i++){
                    System.out.print("-------");
                }
                System.out.println();
            }

        }


    }
    */
}