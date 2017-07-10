package logic;

import shared.GameInfo;
import SharedStructures.MoveData;
import SharedStructures.eColor;
import engine.Player.Type;

import java.util.List;
import java.util.Random;

public class ComputerPlayer extends Player {

    public ComputerPlayer(int i_ID, String i_Name, Type i_Type, eColor i_Color){
        super(i_ID, i_Name, i_Type, i_Color);
    }

    @Override
    public void playTurn(Board i_Board, GameInfo i_GameInfo , Square i_ChosenSquare, List<MoveData> i_MarkMoves, List<MoveData> i_PlayersMoves) {

        Random r = new Random();
        boolean hasNumberInColor = false;

        i_ChosenSquare = null;

        int randomMove = r.nextInt(i_Board.getBoardSize());

        if (i_GameInfo.getGameType().equals(Game.eGameType.Basic.name())){

            if(getPlayerType().name().equals(BasicGame.ePlayerOrientation.COLUMN_PLAYER.name())) {
                while(i_Board.getSquareInPos(randomMove, i_GameInfo.getMarkerCol()).getSquareSymbol().equals("") ||
                        i_Board.getSquareInPos(randomMove, i_GameInfo.getMarkerCol()).getSquareSymbol().equals("@")){
                    randomMove = r.nextInt(i_Board.getBoardSize());
                }

                i_ChosenSquare = i_Board.getSquareInPos(randomMove, i_GameInfo.getMarkerCol());
            }
            else {
                while(i_Board.getSquareInPos(i_GameInfo.getMarkerRow(), randomMove).getSquareSymbol().equals("") ||
                        i_Board.getSquareInPos(i_GameInfo.getMarkerRow(), randomMove).getSquareSymbol().equals("@")){
                    randomMove = r.nextInt(i_Board.getBoardSize());
                }

                i_ChosenSquare = i_Board.getSquareInPos(i_GameInfo.getMarkerRow(), randomMove);
            }

        }
        else {
            //checking color in col
            for(int row = 0; row < i_Board.getBoardSize(); row++){
                if(i_Board.getSquareInPos(row, i_GameInfo.getMarkerCol()).getColor().name().equals(getColor().name())){
                    hasNumberInColor = true;
                    break;
                }
            }
            //finding the relevant square in col
            while(hasNumberInColor && !i_Board.getSquareInPos(randomMove, i_GameInfo.getMarkerCol()).getColor().name().equals(getColor().name())) {
                randomMove = r.nextInt(i_Board.getBoardSize());
            }
            //saving chosen square
            if(hasNumberInColor){
                i_ChosenSquare = i_Board.getSquareInPos(randomMove, i_GameInfo.getMarkerCol());
                i_GameInfo.setChosenCol(i_ChosenSquare.getColumn());
                i_GameInfo.setChosenRow(i_ChosenSquare.getRow());
            }

            //couldn't find color in col
            if (!hasNumberInColor){
                //checking color in row
                for(int col = 0; col < i_Board.getBoardSize(); col++){
                    if(i_Board.getSquareInPos(i_GameInfo.getMarkerRow(), col).getColor().name().equals(getColor().name())){
                        hasNumberInColor = true;
                        break;
                    }
                }
                //finding the relevant square in row
                while( hasNumberInColor && !i_Board.getSquareInPos(i_GameInfo.getMarkerRow(), randomMove).getColor().name().equals(getColor().name())) {
                    randomMove = r.nextInt(i_Board.getBoardSize());
                }

                //saving chosen square
                if(hasNumberInColor){
                    i_ChosenSquare = i_Board.getSquareInPos(i_GameInfo.getMarkerRow(), randomMove);
                    i_GameInfo.setChosenCol(i_ChosenSquare.getColumn());
                    i_GameInfo.setChosenRow(i_ChosenSquare.getRow());
                }
            }
        }

        i_MarkMoves.add(new MoveData(i_Board.getMark().getRow(), i_Board.getMark().getColumn(), eColor.BLACK, i_Board.getMark().getSquareSymbol(), true));
        i_PlayersMoves.add(new MoveData(i_ChosenSquare.getRow(), i_ChosenSquare.getColumn(), i_ChosenSquare.getColor(), i_ChosenSquare.getSquareSymbol(), true));
        super.playTurn(i_Board, i_GameInfo , i_ChosenSquare, i_MarkMoves, i_PlayersMoves);
    }

    private boolean illegalSquareToChoose( Board i_Board, int i_Row, int i_Col ){

        return i_Board.getSquareInPos(i_Row, i_Col).getSquareSymbol().equals("") ||
                i_Board.getSquareInPos(i_Row, i_Col).getSquareSymbol().equals("@") ||
                !i_Board.getSquareInPos(i_Row, i_Col).getColor().name().equals(getColor().name());

    }
}


