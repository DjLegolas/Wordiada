package shared;


import SharedStructures.MoveData;
import SharedStructures.eColor;
import SharedStructures.PlayerData;
import SharedStructures.SquareData;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class GameInfo {
    private int m_RangeFrom;
    private int m_RangeTo;
    private int m_BoardSize;
    private SquareData[][] m_Board;
    private PlayerData m_CurrentPlayer;
    private int m_NumOfMoves;
    private String m_Path;
    private String m_GameTitle;
    private boolean m_isGameActive = false;

    private String m_Organizer;

    private int m_ElapsedTime;
    private int m_RowPlayerScore;
    private int m_ColPlayerScore;
    private int m_MarkerRow;
    private int m_MarkerCol;
    private String m_GameType;
    private String m_BoardStructure;
    private int m_GameMode;
    private ArrayList<PlayerData> m_Players = new ArrayList<>();
    private  int m_NumOfPlayers ;
    private int m_ChosenRow;
    private int m_ChosenCol;
    protected ArrayList<MoveData> m_PlayersMoves = new ArrayList<MoveData>();
    private ArrayList<MoveData> m_MarkMoves = new ArrayList<MoveData>();
    private boolean m_ErrorFound = false;
    private String m_ErrorMsg = "";
    private int m_TotalPlayers;

    private boolean m_TechnicalVictory;
    private String m_WinnerName;
    private boolean m_FinishAllRound = false ;
    private boolean m_GameEmptyOfPlayers  = false;


    public void setTechnicalVictory(boolean m_TechnicalVictory) {
        this.m_TechnicalVictory = m_TechnicalVictory;
    }

    public int getTotalPlayers() { return m_TotalPlayers; }

    public void setTotalPlayers(int players) { m_TotalPlayers = players; }

    public boolean isTechnicalVictory() {
        return m_TechnicalVictory;
    }

    public String getWinnerName() {
        return m_WinnerName;
    }

    public void setWinnerName(String m_WinnerName) {
        this.m_WinnerName = m_WinnerName;
    }

    public boolean isFinishAllRound() {
        return m_FinishAllRound;
    }

    public void setFinishAllRound(boolean m_FinishAllRound) {
        this.m_FinishAllRound = m_FinishAllRound;
    }

    public ArrayList<MoveData> getMarkMoves() {
        return m_MarkMoves;
    }

    public void setMarkMoves(ArrayList<MoveData> i_MarkMoves) {
        this.m_MarkMoves = i_MarkMoves;
    }

    public ArrayList<MoveData> getPlayersMoves() {
        return m_PlayersMoves;
    }

    public void setPlayersMoves(ArrayList<MoveData> i_PlayersMoves) {
        this.m_PlayersMoves = i_PlayersMoves;
    }

    public String getPath() {
        return m_Path;
    }

    public void addPlayerData(PlayerData i_Player){
        m_Players.add(i_Player);
        m_NumOfPlayers++;
    }

    public void setOrganizer(String m_Organizer) {
        this.m_Organizer = m_Organizer;
    }

    public String getOrganizer() {
        return m_Organizer;
    }

    public boolean isActiveGame() { return m_isGameActive; }

    public void setActiveGame(boolean val) { m_isGameActive = val; }

    public ArrayList<PlayerData> getPlayers() {
        return m_Players;
    }

    public int getChosenCol() {
        return m_ChosenCol;
    }

    public int getChosenRow() {
        return m_ChosenRow;
    }

    public void setGameTitle(String title) { m_GameTitle = title; }

    public String getGameTitle() { return m_GameTitle; }

    public void setChosenCol(int i_ChosenCol) {
        this.m_ChosenCol = i_ChosenCol;
    }

    public void setChosenRow(int i_ChosenRow) {
        this.m_ChosenRow = i_ChosenRow;
    }

    public PlayerData getPlayer(int i_Index){
        return m_Players.get(i_Index);
    }

    public void setPath(String i_Path) {
        this.m_Path = i_Path;
    }

    public int getGameMode() {
        return m_GameMode;
    }

    public void setGameMode(int i_GameMode) {
        this.m_GameMode = i_GameMode;
    }

    public int getNumOfPlayers() {
        return m_NumOfPlayers;
    }

    public void setNumOfPlayers(int i_NumOfPlayers) {
        m_NumOfPlayers = i_NumOfPlayers;
    }

    public SquareData getValueInPos(int i_Row, int i_Col) {
        return m_Board[i_Row][i_Col];
    }

    public String getGameType() {
        return m_GameType;
    }

    public String getBoardStructure() {
        return m_BoardStructure;
    }

    public int getMarkerCol() {
        return m_MarkerCol;
    }

    public int getMarkerRow() {
        return m_MarkerRow;
    }

    public SquareData[][] getBoard() {
        return m_Board;
    }

    public int getBoardSize() {
        return m_BoardSize;
    }

    public int getRangeFrom() {
        return m_RangeFrom;
    }

    public int getRangeTo() {
        return m_RangeTo;
    }

    public int getNumOfMoves() {
        return m_NumOfMoves;
    }

    public int getColPlayerScore() {
        return m_ColPlayerScore;
    }

    public int getRowPlayerScore() {
        return m_RowPlayerScore;
    }

    public int getElapsedTime() {
        return m_ElapsedTime;
    }

    public void setColPlayerScore(int i_ColPlayerScore) {
        this.m_ColPlayerScore = i_ColPlayerScore;
    }

    public void setElapsedTime(int i_ElapsedTime) {
        this.m_ElapsedTime = i_ElapsedTime;
    }

    public void setNumOfMoves(int i_NumOfMoves) {
        this.m_NumOfMoves = i_NumOfMoves;
    }

    public void setRowPlayerScore(int i_RowPlayerScore) {
        this.m_RowPlayerScore = i_RowPlayerScore;
    }

    public void setBoard(SquareData[][] i_Board) {
        this.m_Board = i_Board;
    }

    public void setBoardSize(int i_BoardSize) {
        this.m_BoardSize = i_BoardSize;
    }

    public void setRangeFrom(int i_RangeFrom) {
        this.m_RangeFrom = i_RangeFrom;
    }

    public void setRangeTo(int i_setRangeTo) {
        this.m_RangeTo = i_setRangeTo;
    }

    public void setCurrPlayer(PlayerData i_CurrPlayer) {
        this.m_CurrentPlayer = i_CurrPlayer;
    }

    public void setMarkerCol(int i_MarkerCol) {
        this.m_MarkerCol = i_MarkerCol;
    }

    public void setMarkerRow(int i_MarkerRow) {
        this.m_MarkerRow = i_MarkerRow;
    }

    public void setGameType(String i_GameType) {
        this.m_GameType = i_GameType;
    }

    public void setBoardStructure(String i_BoardStructure) {
        this.m_BoardStructure = i_BoardStructure;
    }

    public void setSquare(int i_Row, int i_Col, String i_Str, int i_Color) {
        m_Board[i_Row][i_Col].setValue(i_Str);
        m_Board[i_Row][i_Col].setColor(eColor.values()[i_Color]);

        if(i_Str.equals("@")) {
            m_MarkerRow = i_Row;
            m_MarkerCol = i_Col;
        }
    }

    public void initBoard(){
        m_Board = new SquareData[m_BoardSize][m_BoardSize];
        for(int i = 0; i < m_BoardSize; i++){
            for(int j = 0; j < m_BoardSize; j++){
                m_Board[i][j] = new SquareData(i, j, eColor.BLACK, "");
            }
        }
    }

    public PlayerData getCurrPlayer() {
        return m_CurrentPlayer;
    }

    public void setMove(SquareData i_Square) {
        setChosenRow(i_Square.getRow());
        setChosenCol(i_Square.getCol());
    }

    public void resetPlayersData(){

        /*for (PlayerData player : m_Players) {
            player.setScore(0);
            player.setName("");
            player.setID(1);
            player.setColor(eColor.BLACK);
        }*/

        m_Players =  new ArrayList<PlayerData>();

    }

    public int getIndexOfPlayer(PlayerData currPlayer) {
        int i;
        for(i = 0; i < m_NumOfPlayers; i++){
            if (currPlayer.getID() == m_Players.get(i).getID()) {
                return i;
            }
        }
        return -1;
    }

    public void removeCurrentPlayer() {
        for(PlayerData player: m_Players){
            if(player.getID() == m_CurrentPlayer.getID()){
                m_Players.remove(player);
                break;
            }
        }
    }

    public void setErrorFound(boolean errorFound) {
        this.m_ErrorFound = errorFound;
    }

    public boolean getErrorFound() {
        return m_ErrorFound;
    }

    public void setErrorMsg(String errorMsg) {
        this.m_ErrorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return m_ErrorMsg ;
    }

    public void setGameEmptyOfPlayers(boolean gameEmptyOfPlayers) {
        this.m_GameEmptyOfPlayers = gameEmptyOfPlayers;
    }

    public boolean getGameEmptyOfPlayers() {
        return m_GameEmptyOfPlayers;
    }

    public void removePlayer(String username) {
        for(PlayerData player: m_Players){
            if(player.getName().equals(username)){
                m_Players.remove(player);
                break;
            }
        }
    }
}
