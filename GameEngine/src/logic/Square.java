package logic;
import SharedStructures.eColor;

public class Square {
    private int m_Row;
    private int m_Column;
    private String m_SquareSymbol;
    private eColor m_Color;

    public Square(int i_Row, int i_Column, String i_SquareSymbol, eColor i_Color ){
        m_Row = i_Row;
        m_Column = i_Column;
        m_SquareSymbol = i_SquareSymbol;
        m_Color = i_Color;
    }

    public Square() {
        m_Column = 0;
        m_Row = 0;
        m_SquareSymbol = "";
        m_Color = eColor.BLACK;
    }

    //GET METHODS
    public String getSquareSymbol() {
        return m_SquareSymbol;
    }

    //GET METHODS
    public void setSquareSymbol(String i_SquareSymbol) {
        m_SquareSymbol = i_SquareSymbol;
    }

    //GET METHODS
    public int getColumn() {
        return m_Column;
    }

    public void setColumn(int i_Col) {
        m_Column = i_Col;
    }

    public int getRow() {
        return m_Row;
    }

    public void setRow(int i_Row){
        m_Row = i_Row;
    }


    public void swapSquare(Square i_markToChange) {
        String symbolTemp;
        eColor colorTemp;

        symbolTemp = i_markToChange.getSquareSymbol();
        colorTemp = i_markToChange.getColor();

        i_markToChange.setSquareSymbol(this.getSquareSymbol());

        this.setSquareSymbol(symbolTemp);
        this.setColor(colorTemp);
    }

    public eColor getColor() {
        return m_Color;
    }

    public void setColor(eColor i_Color) {
        this.m_Color = i_Color;
    }
}
