package logic;

import shared.GameInfo;
import SharedStructures.MoveData;
import SharedStructures.eColor;
import engine.Player.Type;

import java.util.List;


public class HumanPlayer extends  Player {

    public HumanPlayer(int i_ID, String i_Name, engine.Player.Type i_Type, eColor i_Color){
        super(i_ID, i_Name, i_Type, i_Color);
    }

    @Override
    public void playTurn(Board i_Board, GameInfo i_GameInfo , Square i_ChosenSquare, List<MoveData> i_MarkMoves, List<MoveData> i_PlayersMoves) {
        i_MarkMoves.add(new MoveData(i_Board.getMark().getRow(), i_Board.getMark().getColumn(), eColor.BLACK, i_Board.getMark().getSquareSymbol(), true));
        i_PlayersMoves.add(new MoveData(i_ChosenSquare.getRow(), i_ChosenSquare.getColumn(), i_ChosenSquare.getColor(), i_ChosenSquare.getSquareSymbol(), true));
        super.playTurn(i_Board, i_GameInfo , i_ChosenSquare, i_MarkMoves, i_PlayersMoves);
    }
}
