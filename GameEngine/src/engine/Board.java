package engine;

import engine.exceptions.OutOfBoardBoundariesException;
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

        @Override
        public int hashCode() {
            int hash = 17;
            hash = 13 * hash + x + y;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this){
                return true;
            }
            if (!(obj instanceof Point)) {
                return false;
            }

            final Point other = (Point) obj;
            return this.x == other.x && this.y == other.y;
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
    private List<GameDataFromXml.DataLetter> kupa = new ArrayList<>();
    private Cell [][] board; // for priting
    private Map <Letter,List<Point>> initLetters = new HashMap<>(); //for changes during the game
    static final short MAX_SIZE = 50;
    static final short MIN_SIZE = 5;

    public char[][] getBoard() {
        char[][] board = new char[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = this.board[row][col].isShown ? this.board[row][col].sign.toCharArray()[0] : ' ';
            }
        }
        return board;
    }


    //C'tor
    Board(short _size, List<GameDataFromXml.DataLetter> letters, int totalAmountLetters){

        Letter toAdd;
        Point p;
        this.board = new Cell[_size][_size];
        size = _size;

        //(x,y) point in the board
        //if  equal amount letters to board size
        if(totalAmountLetters == _size * _size)
        {
            for(GameDataFromXml.DataLetter letter : letters){
                toAdd = letter.getLetter();
                if (!initLetters.containsKey(toAdd)) {
                    initLetters.put(toAdd, new ArrayList<>());
                }
                for(int i = 0 ; i < letter.getAmount(); i++){
                    p = getRandomPoint();
                    initLetters.get(toAdd).add(p);
                    board[p.getY()][p.getX()] = new Cell(toAdd.getSign().get(0),false);
                    letter.setAmount(letter.getAmount() - 1);
                }
            }
        }
        //more letters than board size - need for kupa
        else{
            int numOfInsertions = 0;
            while (numOfInsertions < size*size){
                for(GameDataFromXml.DataLetter letter : letters){
                    if(numOfInsertions < size*size){
                        if(letter.getAmount()>0){
                            toAdd = letter.getLetter();
                            if (!initLetters.containsKey(toAdd)) {
                                initLetters.put(toAdd, new ArrayList<>());
                            }
                            p = getRandomPoint();
                            initLetters.get(toAdd).add(p);
                            board[p.getY()][p.getX()] = new Cell(toAdd.getSign().get(0),false);
                            numOfInsertions ++;
                            letter.setAmount(letter.getAmount() - 1);
                        }
                    }
                }
            }

            //build the kupa
            kupa.addAll(letters);
        }
    }

    private Point getRandomPoint() {
        int x,y;
        Random xy = new Random();
        Point p;
        boolean toContinue;
        do {
            toContinue = false;
            x = xy.nextInt(size);
            y = xy.nextInt(size);
            p = new Point(x,y);
            for (List<Point> listPoints : initLetters.values()){
                if (listPoints.contains(p))
                    toContinue = true;
            }
        } while (toContinue);
        return p;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public List<GameDataFromXml.DataLetter> getKupa() {
        return kupa;
    }
    public int getKupaAmount(){
        int amount = 0;
        for(GameDataFromXml.DataLetter l : kupa){
            amount += l.getAmount();
        }
        return amount;
    }

    public void update(List<int[]> points) throws OutOfBoardBoundariesException {

        //check valid point

        for(int i = 0; i < points.size(); i++){
            Point point = new Point((points.get(i))[1], (points.get(i))[0]);

            if(point.getX() > size || point.getX() < 1 || point.getY() > size || point.getY() < 1){
                throw new OutOfBoardBoundariesException();
            }
            else{
                board[point.getY()-1][point.getX()-1].isShown = true;
            }
        }
    }

    public boolean hasChars(String word) {
        for (Character c: word.toCharArray()) {
            boolean hasChar = false;
            for (Letter letter: initLetters.keySet()) {
                if (letter.getSign().get(0).equals(c.toString())) {
                    for (Point point: initLetters.get(letter)) {
                        if (board[point.getY()][point.getX()].isShown) {
                            hasChar = true;
                            break;
                        }
                    }
                    if (!hasChar) {
                        return false;
                    }
                }
                if (hasChar) {
                    break;
                }
            }
        }
        return true;
    }

    public void removeLettersFromBoard(String word) {
        Random random = new Random();
        List<String> chars = new ArrayList<>();
        for (Character c: word.toCharArray()) {
            chars.add(c.toString());
        }
        boolean stop = false;
        for (int row = 0; row < size && !stop; row++) {
            for (int col = 0; col < size && !stop; col++) {
                if (!chars.isEmpty()) {
                    if (board[row][col].isShown && chars.contains(board[row][col].sign)) {
                        chars.remove(board[row][col].sign);
                        for (Letter letter: initLetters.keySet()) {
                            if (letter.getSign().get(0).equals(board[row][col].sign)) {
                                initLetters.get(letter).remove(new Point(col, row));
                                break;
                            }
                        }

                        // add new letter to the board
                        GameDataFromXml.DataLetter dataLetter;
                        do {
                            int letter = random.nextInt(initLetters.size());
                            dataLetter = kupa.get(letter);
                        } while(!(dataLetter.getAmount() > 0));
                        Letter letter = dataLetter.getLetter();
                        initLetters.get(letter).add(new Point(col, row));
                        dataLetter.setAmount(dataLetter.getAmount() - 1);
                        board[row][col].sign = letter.getSign().get(0);
                        board[row][col].isShown = false;
                    }
                }
                else {
                    stop = true;
                }
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