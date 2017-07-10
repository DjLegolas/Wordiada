package logic;
import SharedStructures.eColor;

import java.util.ArrayList;

public class Board {

    private int m_BoardSize;
    private ArrayList<ArrayList<Square>> m_Board;
    private Square m_Mark;
    private Range m_Range;

    private class Range{

        private int m_From;
        private int m_To;


        public Range (int i_From, int i_To){
            m_From = i_From;
            m_To = i_To;
        }

        public void setFrom(int i_From) {
            this.m_From = i_From;
        }

        public void setTo(int i_To) {
            this.m_To = i_To;
        }

        public int getTo() {
            return m_To;
        }

        public int getFrom() {
            return m_From;
        }
    }

    public Board(int i_BoardSize) {

        m_BoardSize = i_BoardSize;
        m_Board = new ArrayList<ArrayList<Square>>() ;

        for(int i = 0; i < m_BoardSize; i++){
            m_Board.add(new ArrayList<Square>()) ;
        }

        int i = 0;
        for (ArrayList<Square> row : m_Board) {
            for(int j = 0; j < m_BoardSize; j++) {
                row.add(new Square(i, j, "", eColor.BLACK));
            }
            i++;
        }


    }

    public Square getMark() {
        return m_Mark;
    }

    public Square getSquareInPos(int i, int j) {

        return m_Board.get(i).get(j);
    }

    public void createRange(int i_From, int i_To) {
        m_Range = new Range(i_From,i_To);
    }

    public void createMarker(int i_MarkerRow, int i_MarkerCol) {
        //m_Mark = new Square(i_MarkerRow, i_MarkerCol, "@", eColor.DEFAULT);
        m_Mark = new Square(i_MarkerRow, i_MarkerCol, "@", eColor.BLACK);
    }

    public int getToRange(){
        return m_Range.m_To;
    }

    public int getFromRange(){
        return m_Range.m_From;
    }

    public int getMarkerCol() {
        return m_Mark.getColumn();
    }

    public int getMarkerRow() {
        return m_Mark.getRow();
    }

    public int getBoardSize() {
        return m_BoardSize;
    }

    public void changeMarker(Square currMarker, Square currChosenSquare) {
        currMarker.swapSquare(currChosenSquare);
        currMarker.setSquareSymbol("");
        currMarker.setColor(eColor.BLACK);
        currChosenSquare.setColor(eColor.BLACK);
        setMark(currChosenSquare);
    }

    public void setMark(Square i_Mark) {
        this.m_Mark = i_Mark;
    }
}
