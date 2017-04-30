package engine;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import engine.exceptions.BoardSizeException;
import engine.jaxb.schema.generated.Letter;

import java.util.Random;
import engine.jaxb.*;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import engine.jaxb.sdf;

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
    Cell [][] board;
  //  Map <String,Point> ShownLetters = new HashMap<>();
    Map <Cell,List<Point>> initLettrs = new HashMap<>();

    public Cell [][] getBoard() {return board;}


    //test:
    public static void main(String [] argv){
     Board b = new Board((short)6);
     b.printBoard(b.getBoard());
        System.out.print('\r');

     }


    //C'tor
    Board(short _size, List<GameDataFromXml.DataLetter> letters, int totalAmountLetters){

        int x,y;
        Cell toAdd;
        Point p;
        List<Point> usedPoints =new ArrayList<>();
        this.board = new Cell[_size][_size];
        size = _size;

        //(x,y) point in the board
        Random xy = new Random();
        //if less or equal amount letters than board
        if(totalAmountLetters <= _size * _size)
        {
            for(GameDataFromXml.DataLetter letter : letters){
                toAdd =new Cell(letter.getLetter().getSign().get(0), false);
                List<Point> empty = new ArrayList<>();
                initLettrs.put(toAdd, empty );

                for(int i = 0 ; i < letter.getAmount(); i++){
                    do {
                        x = xy.nextInt(size);
                        y = xy.nextInt(size);
                        p = new Point(x,y);
                    } while (initLettrs.containsValue(p));
                    initLettrs.get(toAdd).add(p);
                    }
            }
        }
        //more letters than board size
        else{


        }

    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }
/*
    public void printLetterInBoard(char sign){
        System.out.println(String.format("%-30s","h"));
    }*/


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
}