package logic;

import shared.GameInfo;
import SharedStructures.PlayerData;
import engine.Player.Type;
import java.util.ArrayList;

public class BasicGame extends Game {

    enum ePlayerOrientation{
        ROW_PLAYER, COLUMN_PLAYER
    }

    @Override
    public boolean checkIfNotPossibleMove() {
        boolean turnDone = true;
        Square squareToCheck;

        if(m_CurrentPlayer.getID() == 0) {
            for (int i = 0; i < m_Board.getBoardSize(); i++) {
                //check if row is empty of numbers
                squareToCheck = m_Board.getSquareInPos(m_Board.getMark().getRow(), i);
                if (!squareToCheck.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol()) &&
                        !m_Board.getSquareInPos(m_Board.getMark().getRow(), i).getSquareSymbol().equals("")) {
                    turnDone = false;
                    break;
                }
            }
        }

        else {
            for (int i = 0; i < m_Board.getBoardSize(); i++) {
                //check if column is empty of numbers
                squareToCheck = m_Board.getSquareInPos(i, m_Board.getMark().getColumn());
                if (!squareToCheck.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol()) &&
                        !m_Board.getSquareInPos(i, m_Board.getMark().getColumn()).getSquareSymbol().equals("")) {
                    turnDone = false;
                    break;
                }
            }
        }

        return turnDone;
    }

    @Override
    public void removeCurrentPlayerCellsFromBoard() {}

    public BasicGame(GameInfo[] i_GameInfoWrapper) {
        super(i_GameInfoWrapper);
    }

    @Override
    void setPlayers() {
        m_Players = new ArrayList<Player>();
        String name = "Row player";
        for(int i = 0; i < m_GameInfo.getNumOfPlayers(); i++){
            m_Players.add(new Player(i, name, Type.HUMAN));
            name = "Column player";
        }

        loadPlayersToGameInfo();
    }

    private void loadPlayersToGameInfo() {
        for(int i = 0; i < m_GameInfo.getNumOfPlayers(); i++){
            m_GameInfo.getPlayers().add(new PlayerData(m_Players.get(i).getName(), m_Players.get(i).getID(),m_Players.get(i).getPlayerType(), m_Players.get(i).getPlayerScore()));
        }
    }

    @Override
    protected void initRandomBoardSquare(int randomRow, int randomCol, int colorIndex, int data) {
        m_Board.getSquareInPos(randomRow,randomCol).setSquareSymbol(Integer.toString(data));
    }

    @Override
    boolean checkIfLegalMove(Player i_Player, Square i_Move) {
        boolean res = false;
        if(i_Player.getID() == ePlayerOrientation.COLUMN_PLAYER.ordinal() && i_Move.getColumn() == m_Board.getMark().getColumn()) {
            if (!i_Move.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol())
                    && !i_Move.getSquareSymbol().equals("")) {
                res = true;
            }

        }
        else if(i_Player.getID() == ePlayerOrientation.ROW_PLAYER.ordinal() && i_Move.getRow() == m_Board.getMark().getRow()){
            if (!i_Move.getSquareSymbol().equals(m_Board.getMark().getSquareSymbol())
                    && !i_Move.getSquareSymbol().equals("")) {
                res = true;
            }
        }
        return res;
    }

    @Override
    public boolean checkIfGameDone() {
        boolean gameDone = true;

        if(m_CurrentPlayer.equals(m_Players.get(0))){
            for(int i = 0; i < m_Board.getBoardSize(); i++) {
                //check if row is empty of numbers
                if (!m_Board.getSquareInPos(m_Board.getMark().getRow(), i).getSquareSymbol().equals(m_Board.getMark().getSquareSymbol()) &&
                        !m_Board.getSquareInPos(m_Board.getMark().getRow(), i).getSquareSymbol().equals("")) {
                    gameDone = false;
                    break;
                }
            }
        }
        else{
            for(int i = 0; i < m_Board.getBoardSize(); i++){
                //check if column is empty of numbers
                if(!m_Board.getSquareInPos(i,m_Board.getMark().getColumn()).getSquareSymbol().equals(m_Board.getMark().getSquareSymbol()) &&
                        !m_Board.getSquareInPos(i, m_Board.getMark().getColumn()).getSquareSymbol().equals("")){
                    gameDone = false;
                    break;
                }
            }
        }
        return gameDone;
    }

    @Override
    public void playerQuit() {
        //nothing special to update here. Abstract method is for advanced game
    }

}
