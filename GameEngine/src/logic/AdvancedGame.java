package logic;

import shared.GameInfo;
import SharedStructures.MoveData;
import SharedStructures.eColor;

public class AdvancedGame extends Game {

    @Override
    public boolean checkIfNotPossibleMove() {

        boolean turnDone = true;
        Square squareToCheck;

        for(int i = 0; i < m_Board.getBoardSize(); i++) {
            //check if row is empty of numbers
            squareToCheck = m_Board.getSquareInPos(m_Board.getMark().getRow(), i);
            if (!squareToCheck.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol()) &&
                    !m_Board.getSquareInPos(m_Board.getMark().getRow(), i).getSquareSymbol().equals("") &&
                    squareToCheck.getColor().name().equals(m_CurrentPlayer.getColor().name())) {
                turnDone = false;
                break;
            }
        }

        for(int i = 0; i < m_Board.getBoardSize(); i++){
            //check if column is empty of numbers
            squareToCheck = m_Board.getSquareInPos(i,m_Board.getMark().getColumn());
            if(!squareToCheck.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol()) &&
                    !m_Board.getSquareInPos(i, m_Board.getMark().getColumn()).getSquareSymbol().equals("") &&
                    squareToCheck.getColor().name().equals(m_CurrentPlayer.getColor().name())){
                turnDone = false;
                break;
            }
        }

        return turnDone;
    }

    @Override
    public void removeCurrentPlayerCellsFromBoard() {
        for(int i = 0; i < m_Board.getBoardSize(); i++){
            for(int j = 0; j < m_Board.getBoardSize(); j++){
                if(m_Board.getSquareInPos(i,j).getColor() == m_CurrentPlayer.getColor()){
                    m_PlayersMoves.add(new MoveData(i, j, m_Board.getSquareInPos(i,j).getColor(), m_Board.getSquareInPos(i,j).getSquareSymbol(), false));
                    m_Board.getSquareInPos(i,j).setSquareSymbol("");
                    m_Board.getSquareInPos(i,j).setColor(eColor.BLACK);
                }
            }
        }

        loadBoardToGameInfo();
    }

    public AdvancedGame(GameInfo[] i_GameInfoWrapper) {

        super(i_GameInfoWrapper);
    }

    @Override
    void setPlayers() {
        //m_Players = new ArrayList<Player>();
        for(int i = 0; i < m_GameInfo.getNumOfPlayers(); i++) {
            m_Players.add(Player.CreatePlayer(m_GameInfo.getPlayer(i)));
        }
        m_CurrentPlayer = m_Players.get(0);
    }

    @Override
    protected void initRandomBoardSquare(int randomRow, int randomCol, int colorIndex, int data) {
        m_Board.getSquareInPos(randomRow,randomCol).setSquareSymbol(Integer.toString(data));
        m_Board.getSquareInPos(randomRow,randomCol).setColor(eColor.values()[colorIndex]);
    }

    @Override
    boolean checkIfLegalMove(Player i_Player, Square i_Move) {
        boolean res = true;

        if(i_Player.getColor() != i_Move.getColor()){
            res = false;
        }
        else if(i_Move.getColumn() == m_Board.getMark().getColumn()) {
            if (i_Move.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol())
                    || i_Move.getSquareSymbol().equals("")
                    || i_Move.getColor() != i_Player.getColor()) {
                res = false;
            }
        }
        else if(i_Move.getRow() == m_Board.getMark().getRow()){
            if (i_Move.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol())
                    || i_Move.getSquareSymbol().equals("")) {
                res = false;
            }
        }
        else if(i_Move.getColumn() != m_Board.getMark().getColumn() && i_Move.getRow() != m_Board.getMark().getRow()){
            res = false;
        }

        return res;
    }

    @Override
    public boolean checkIfGameDone() {
        boolean gameDone = true;
        Square squareToCheck;

        for(int i = 0; i < m_Board.getBoardSize(); i++) {
            //check if row is empty of numbers
            squareToCheck = m_Board.getSquareInPos(m_Board.getMark().getRow(), i);
            if (!squareToCheck.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol()) &&
                    !m_Board.getSquareInPos(m_Board.getMark().getRow(), i).getSquareSymbol().equals("")) {
                gameDone = false;
                break;
            }
        }

        for(int i = 0; i < m_Board.getBoardSize(); i++){
            //check if column is empty of numbers
            squareToCheck = m_Board.getSquareInPos(i,m_Board.getMark().getColumn());
            if(!squareToCheck.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol()) &&
                    !m_Board.getSquareInPos(i, m_Board.getMark().getColumn()).getSquareSymbol().equals("")){
                gameDone = false;
                break;
            }
        }

        return gameDone;
    }

    @Override
    public void playerQuit() {
        Square square = new Square();
        eColor currPlayerColor = m_CurrentPlayer.getColor();
        for(int row = 0; row < m_Board.getBoardSize(); row++){
            for(int col = 0; col < m_Board.getBoardSize(); col++){
                square = m_Board.getSquareInPos(row,col);
                if(square.getColor() == currPlayerColor){
                    square.setSquareSymbol("");
                    square.setColor(eColor.BLACK);
                }
            }
        }
    }
}
