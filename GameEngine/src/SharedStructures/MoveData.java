package SharedStructures;

public class MoveData {
    private int m_Row;
    private int m_Col;
    private eColor m_Color;
    private String m_Value;
    private boolean m_IsAlive;

    public MoveData(int i_Row, int i_Col, eColor i_Color, String i_Value, boolean i_IsAlive){
        m_Row = i_Row;
        m_Col = i_Col;
        m_Color = i_Color;
        m_Value = i_Value;
        m_IsAlive = i_IsAlive;
    }

    public MoveData(){}

    public MoveData(MoveData moveData) {
        m_Row = moveData.getRow();
        m_Col = moveData.getCol();
        m_Color = moveData.getColor();
        m_Value = moveData.getValue();
        m_IsAlive = moveData.isAlive();
    }

    public int getRow() {
        return m_Row;
    }

    public int getCol() {
        return m_Col;
    }

    public void setCol(int i_Col) {
        this.m_Col = i_Col;
    }

    public void setRow(int i_Row) {
        this.m_Row = i_Row;
    }

    public void setColor(eColor i_Color) {
        this.m_Color = i_Color;
    }

    public eColor getColor() {
        return m_Color;
    }

    public String getValue() {
        return m_Value;
    }

    public void setType(String i_Value) {
        this.m_Value = i_Value;
    }

    public boolean isAlive() {
        return m_IsAlive;
    }
}
