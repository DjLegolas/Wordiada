package logic;

import shared.GameInfo;
import SharedStructures.MoveData;
import SharedStructures.eColor;
import SharedStructures.PlayerData;
import engine.Player.Type;

import java.util.List;

public abstract class Player {

    private int m_Score = 0;
    private int m_ID;
    private String m_Name;
    private Type m_PlayerType;
    private eColor m_Color;

    public Player(){}

    public Player(int i_ID, String i_Name, Type i_Type, eColor i_Color){
        m_ID = i_ID;
        m_Name = i_Name;
        m_Color = i_Color;
        m_PlayerType = i_Type;
    }

    public void setID(int i_ID) {
        this.m_ID = i_ID;
    }

    public int getID() {
        return m_ID;
    }

    public String getName() {
        return m_Name;
    }

    public void setName(String i_Name) {
        this.m_Name = i_Name;
    }

    public void playTurn(Board i_Board, GameInfo i_GameInfo , Square i_ChosenSquare, List<MoveData> i_MarkMoves, List<MoveData> i_PlayersMoves) {
        if ( i_ChosenSquare != null ){
            addToPlayerScore(Integer.parseInt(i_ChosenSquare.getSquareSymbol()));
            updatePlayerScoreInGameInfo(this, i_GameInfo);
            i_Board.changeMarker(i_Board.getSquareInPos(i_Board.getMark().getRow(), i_Board.getMark().getColumn()), i_Board.getSquareInPos(i_ChosenSquare.getRow(), i_ChosenSquare.getColumn()));
        }
    }

    private void updatePlayerScoreInGameInfo(Player i_Player, GameInfo i_gameInfo) {
        for(PlayerData player: i_gameInfo.getPlayers()){
            if(player.getID() == i_Player.getID()){
                player.setScore(i_Player.getPlayerScore());
            }
        }
    }


    Type getPlayerType() {
        return m_PlayerType;
    }

    void setPlayerType(Type i_PlayerType) {
        this.m_PlayerType = i_PlayerType;
    }

    public int getPlayerScore() {
        return m_Score;
    }

    public void addToPlayerScore(int i_PointsToAdd){
        m_Score += i_PointsToAdd;
    }

    public static Player CreatePlayer(PlayerData i_Player) {
        Player res;
        if(i_Player.getType().name().equals(Type.HUMAN.name())){
            res = new HumanPlayer(i_Player.getID(), i_Player.getName(), i_Player.getType(), i_Player.getColor());
        }
        else{
            res = new ComputerPlayer(i_Player.getID(), i_Player.getName(), i_Player.getType(), i_Player.getColor());
        }

        return res;
    }

    public eColor getColor() {
        return m_Color;
    }
}
