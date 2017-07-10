package SharedStructures;

public class SquareData {
    private int m_Row;
    private int m_Col;
    private eColor m_Color;
    private String m_Value;

    public SquareData(int i_Row, int i_Col, eColor i_Color, String i_Value){
        m_Row = i_Row;
        m_Col = i_Col;
        m_Color = i_Color;
        m_Value = i_Value;
    }

    public SquareData() {}

    public void setColor(eColor i_Color) {
        this.m_Color = i_Color;
    }

    public eColor getColor() {
        return m_Color;
    }

    public String getValue() {
        return m_Value;
    }

    public void setValue(String i_Value) {
        this.m_Value = i_Value;
    }

    public int getCol() {
        return m_Col;
    }

    public int getRow() {
        return m_Row;
    }

    public void setCol(int i_Col) {
        this.m_Col = i_Col;
    }

    public void setRow(int i_Row) {
        this.m_Row = i_Row;
    }
}
