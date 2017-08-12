package  engine;

import java.io.File;
import java.io.InputStream;
import java.lang.String;

import engine.exceptions.*;

import java.util.*;

import engine.Board.Point;
import engine.GameDataFromXml.*;

import engine.jaxb.schema.generated.Letter;
import javafx.util.Pair;

public class GameEngine {

    private Player currentPlayer;
    private Player winner;
    private int nextPlayerNumber = 1;
    private int numOfPlayers;
    private List<Player> players;
    private List<GameDataFromXml> gdfx = new ArrayList<>();
    private GameDataFromXml currentGameData;
    private boolean isGameStarted = false;
    private boolean isGameEnded = false;
    private int diceValue = 0;
    private long startTime;
    private int numberOfTurns = 0;
    private int tryNumber;
    private Map <Integer, CaptureTheMoment> turnData = new HashMap<>();
    private int pointerForTurnData = -1;
    public enum WordCheck {
            CORRECT, WRONG, WRONG_CANT_RETRY, CHARS_NOT_PRESENT, TRIES_DEPLETED
    }
    private String uploaderName;

    public static class CaptureTheMoment{
        private  List<Player> players;
        private List<int[]> selectedBoardButtons; //for showing the selected word
        private Player currentPlayer;
        private int turnNumber;
        private char[][] board;

        public CaptureTheMoment(){}

        public void setCurrentPlayer(Player currentPlayer) {
            this.currentPlayer = currentPlayer;
        }

        public void setPlayers(List<Player> players) {
            this.players = new ArrayList<>();
            for (Player player: players) {
                if (player.getId() == currentPlayer.getId()) {
                    this.players.add(currentPlayer);
                }
                else {
                    this.players.add(new Player(player));
                }
            }
        }

        public void setSelectedPoints(List<int[]> selectedBoardButtons) {
            this.selectedBoardButtons = selectedBoardButtons;
        }

        public void setTurnNumber(int turnNumber) {
            this.turnNumber = turnNumber;
        }

        void setBoard(char[][] board) {
            this.board = board;
        }

        public char[][] getBoard() {
            return board;
        }

        public Player getCurrentPlayer() {
            return currentPlayer;
        }

        public List<Player> getPlayers() {
            return players;
        }

        public int getTurnNumber() {
            return turnNumber + 1;
        }

        public List<int[]> getSelectedBoardButtons() {
            return selectedBoardButtons;
        }
    }

    public Map<Integer, CaptureTheMoment> getTurnData() {
        return turnData;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public void loadXml(String pathToXml)
            throws WrongPathException, DictionaryNotFoundException, BoardSizeException, NotXmlFileException,
            DuplicateLetterException, NotValidXmlFileException, WinTypeException, NotEnoughLettersException,
            NumberOfPlayersException, DuplicatePlayerIdException, NoTitleException {
        GameDataFromXml gd = new GameDataFromXml();
        gd.initializeDataFromXml(pathToXml);
        gdfx.add(gd);
    }

    public void loadXml(InputStream file, InputStream dictFile, String dictFileName, String userNameFromSession)
            throws WrongPathException, DictionaryNotFoundException, BoardSizeException, NotXmlFileException,
            DuplicateLetterException, NotValidXmlFileException, WinTypeException, NotEnoughLettersException,
            NumberOfPlayersException, DuplicatePlayerIdException, NoTitleException {
        GameDataFromXml gd = new GameDataFromXml();
        gd.initializeDataFromXml(file, dictFile, dictFileName);
        uploaderName = userNameFromSession;
        gdfx.add(gd);
        players = new ArrayList<>();
    }

    public void loadXml(File file)
            throws WrongPathException, DictionaryNotFoundException, BoardSizeException, NotXmlFileException,
            DuplicateLetterException, NotValidXmlFileException, WinTypeException, NotEnoughLettersException,
            NumberOfPlayersException, DuplicatePlayerIdException {
        GameDataFromXml gd = new GameDataFromXml();
        gd.initializeDataFromXml(file);
        gdfx.add(gd);
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public String getGameTitle() {
        return isGameStarted ? currentGameData.getGameTitle() : gdfx.get(gdfx.size() - 1).getGameTitle();
    }

    private GameDataFromXml getGameData() {
        return isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
    }

    public void reset() {
        currentPlayer = winner = null;
        players.clear();
        isGameStarted = false;
        isGameEnded = false;
        currentGameData.resetBoard();
    }

    public boolean isXmlLoaded() {
        return !gdfx.isEmpty();
    }

    public boolean isStarted() {
        return isGameStarted;
    }

    private List<Player> getPlayersList() {
        GameDataFromXml gd = getGameData();

        if (!gd.isDynamic()) {
            List<Player> players = new ArrayList<>();
            List<engine.jaxb.schema.generated.Player> _players = gd.getPlayers();

            for (engine.jaxb.schema.generated.Player p : _players) {
                players.add(new Player(p.getName().get(0), p.getId(), p.getType()));
            }
            return players;
        }

        return players;
    }

    public boolean isFull() {
        GameDataFromXml gd = getGameData();
        return players.size() == gd.getTotalPlayers();
    }

    public void startGame() {
        isGameStarted = true;
        isGameEnded = false;
        currentGameData =  gdfx.get(gdfx.size() - 1);
        players = getPlayersList();
        numOfPlayers = players.size();
        currentPlayer = players.get(0);
        tryNumber = 1;
        numberOfTurns = 0;
        pointerForTurnData = -1;
        turnData.clear();
        diceValue = 0;
        startTime = System.currentTimeMillis();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isPlayerComputer() {
        return currentPlayer.isComputer();
    }

    public Status getStatus() {
        GameDataFromXml gd = getGameData();

        return new Status(
                gd.getBoard().getBoard_onlySigns(),
                currentPlayer != null ? currentPlayer.getName() : null,
                gd.getBoard().getKupaAmount(),
                currentPlayer != null ? currentPlayer.getScore() : 0,
                currentPlayer != null ? currentPlayer.getId() : 0
        );
    }

    public int getDiceValue() {
        if (diceValue == 0) {
            Random random = new Random();
            diceValue = random.nextInt(currentGameData.getNumOfCubeWigs() - 1) + 2;
        }
        return diceValue;
    }

    public boolean updateBoard(List<int[]> points) throws OutOfBoardBoundariesException {
        if (points.size() > diceValue) {
            return false;
        }
        currentGameData.updateBoard(points);
        return true;
    }

    public char[][] getBoard() {
        GameDataFromXml gd = getGameData();
        return gd.getBoard().getBoard_onlySigns();
    }

    public Board.Cell[][] getBoardAsCells() {
        GameDataFromXml gd = getGameData();
        return gd.getBoard().getBoard();
    }

    public short getBoardSize(){
        GameDataFromXml gameDataFromXml = getGameData();
        return gameDataFromXml.getBoard().getBoardSize();
    }

    private boolean canRetry() {
        return tryNumber <= currentGameData.getNumOfTries() + 1;
    }

    public int getMaxRetries() {
        GameDataFromXml gameDataFromXml = getGameData();
        return gameDataFromXml.getNumOfTries();
    }

    public WordCheck isWordValid(String word, int tries) {
        return isWordValidWithCoordinates(word, tries, null);
    }

    public WordCheck isWordValidWithCoordinates(String word, int tries, List<int[]> selectedPoints) {
        if (tries == tryNumber && canRetry()) {
            if (!currentGameData.getBoard().hasChars(word)) {
                tryNumber++;
                return WordCheck.CHARS_NOT_PRESENT;
            }
            Dictionary.Word dictWord = currentGameData.getDictionary().hasWord(word);
            if (dictWord != null) {
                // this was the old version
                // currentGameData.getBoard().removeLettersFromBoard(word);
                currentPlayer.updateScore(dictWord, currentGameData.calcScore(word));
                tryNumber = 1;
                saveTheTurn(selectedPoints);
                currentGameData.getBoard().removePointsFromBoard(selectedPoints);
                numberOfTurns++;
                diceValue = 0;
                return WordCheck.CORRECT;
            }
            tryNumber++;
            if (!canRetry()) {
                saveTheTurn(new ArrayList<>());
                nextPlayer();
                return WordCheck.WRONG_CANT_RETRY;
            }
            return WordCheck.WRONG;
        }
        saveTheTurn(new ArrayList<>());
        nextPlayer();
        return WordCheck.TRIES_DEPLETED;
    }

    private void nextPlayer() {
        do {
            currentPlayer = players.get(nextPlayerNumber);
            nextPlayerNumber = (nextPlayerNumber + 1) % players.size();
        } while (currentPlayer.isRetired());
        numberOfTurns++;
        tryNumber = 1;
        diceValue = 0;
    }

    public Statistics getStatistics() {
        if (isGameEnded) {
            return new Statistics(isGameStarted, currentGameData, uploaderName, currentPlayer, players, System.currentTimeMillis() - startTime, numberOfTurns + 1, winner);
        }
        if (isGameStarted) {
            return new Statistics(true, currentGameData, uploaderName, currentPlayer, players, System.currentTimeMillis() - startTime, numberOfTurns + 1, null);
        }
        return new Statistics(false, gdfx.get(gdfx.size() - 1), uploaderName, null, getPlayersList(), 0, 0, null);
    }

    public List<Board.Point> getUnShownPoints(){
            List<Board.Point> unShownPoints = new ArrayList<>();
            for(int row = 0; row <currentGameData.getBoard().getBoardSize(); row++){
                for(int col = 0; col < currentGameData.getBoard().getBoardSize(); col++){
                    if(!(currentGameData.getBoard().getBoardFullDetails()[row][col].isShown)) {
                        Point p = new Board.Point (col+1,row+1);
                        unShownPoints.add(p);
                    }
                }
            }
            return unShownPoints;
    }

    public boolean isGameEnded() {
        //if no more left cards in kupa and all the tails in the board are shown
        if (isGameEnded || (numOfPlayers < 2 || currentGameData.getBoard().getKupaAmount() == 0) ||
                (diceValue == 0 && getUnShownPoints().isEmpty())) {
            if (!isGameEnded) {
                setWinner();
                isGameEnded = true;
            }
        }
        return isGameEnded;
    }

    private void setWinner() {
        winner = null;
        if (numOfPlayers < 2) {
            for (Player player: players) {
                if (!player.isRetired()) {
                    winner = player;
                    break;
                }
            }
        }
        else {
            List<Player> players = new ArrayList<>(this.players);
            players.sort((player1, player2) -> (int) (player1.getScore() - player2.getScore()));
            if (players.get(players.size() - 1).getScore() > players.get(players.size() - 2).getScore()) {
                winner = players.get(players.size() - 1);
            }
        }
    }

    public boolean isGameEnded(boolean allTilesShown) {
        if ((numOfPlayers < 2 || currentGameData.getBoard().getKupaAmount() == 0) || allTilesShown) {
            isGameStarted = false;
            return true;
        }
        return false;
    }

    public String getWinnerName(boolean userEnd) {
        Player winner = null;
        // TODO: fix when having more than 2 players
        if (userEnd) {
            winner = players.get(nextPlayerNumber);
        }
        else {
            for (Player player : players) {
                if (winner == null) {
                    winner = player;
                } else if (winner.getScore() < player.getScore()) {
                    winner = player;
                }
            }
        }
        return winner == null ? "" : winner.getName();
    }

    public List<Player> getPlayers() {
        return isGameStarted ? players : getPlayersList();
    }

    public short getCurrentPlayerId() {
        return currentPlayer.getId();
    }

    public Board getBoardObject(){
        return currentGameData.getBoard();
    }

    public Boolean isInGoldFishMod(){
        GameDataFromXml gameDataFromXml = getGameData();
        return gameDataFromXml.getGoldFishMod();
    }

    public WinAccordingTo getWinScoreMod(){
        GameDataFromXml gameDataFromXml = getGameData();
        return gameDataFromXml.getWinAccordingTo();
    }

    public boolean isWordScore(){
        GameDataFromXml gameDataFromXml = getGameData();
        return gameDataFromXml.getWinAccordingTo().equals(WinAccordingTo.WORD_SCORE);
    }

    public String getFreqEachLetter(){
        GameDataFromXml gameDataFromXml = getGameData();
        StringBuilder freqLetters = new StringBuilder();
        for(Letter letter : gameDataFromXml.getBoard().getInitLetters().keySet()) {
            freqLetters.append(letter.getSign().get(0) + " : " + letter.getFrequency()+ "\n");
        }
        return freqLetters.toString();
    }

    public String getTopTenRareWords(){
        GameDataFromXml gameDataFromXml = getGameData();
        return gameDataFromXml.getDictionary().getTop10RareWords();
    }
    public GameDataFromXml getCurrentGameData(){
        return currentGameData;
    }

    public Set<Dictionary.Word> getPlayerWords(String playerName) {
        for (Player player: players) {
            if (player.getName().equals(playerName)) {
                return player.getWords().keySet();
            }
        }
        return null;
    }

    public Map<String, Pair<Integer, Integer>> getPlayerWords(int playerId) {
        // word, amount, score
        Map<String, Pair<Integer, Integer>> words = new HashMap<>();
        List<Player> players = isGameEnded() ? turnData.get(pointerForTurnData).players : this.players;
        for (Player player: players) {
            if (player.getId() == playerId) {
                for (Map.Entry<Dictionary.Word, Integer> entry: player.getWords().entrySet()) {
                    words.put(entry.getKey().getWord(), new Pair<>(entry.getValue(), entry.getKey().getScore()));
                }
                return words;
            }
        }
        return null;
    }

    private boolean doRetire(Player retiredPlayer) {
        retiredPlayer.retire();
        numOfPlayers--;
        if (numOfPlayers == 0) {
            reset();
            return false;
        }
        else if (numOfPlayers < 2) {
            return false;
        }
        if (retiredPlayer == currentPlayer){
            nextPlayer();
        }
        return true;
    }

    public boolean retirePlayer() {
        return doRetire(currentPlayer);
    }

    public String retirePlayer(String playerName) {
        Player player = null;
        for (Player _player: players) {
            if (!isGameStarted) {
                players.remove(_player);
                return null;
            }
            if (_player.getName().equals(playerName)) {
                player = _player;
            }
        }
        Boolean ret;
        try {
            ret = doRetire(player);
        }
        catch (NullPointerException e) {
            return "No player with name \"" + playerName + "\"";
        }
        return ret.toString();
    }

    //TODO: decide where to call this func - should be after each move
    private void saveTheTurn(List<int[]> selectedPoints){
        CaptureTheMoment captureTheMoment = new CaptureTheMoment();
        captureTheMoment.setCurrentPlayer(new Player(currentPlayer));
        captureTheMoment.setPlayers(players);
        captureTheMoment.setTurnNumber(numberOfTurns);
        captureTheMoment.setSelectedPoints(selectedPoints);
        captureTheMoment.setBoard(getBoard());
        turnData.put(numberOfTurns, captureTheMoment);
    }

    public CaptureTheMoment getSpecificTurn(){
        return turnData.get(pointerForTurnData);
    }

    private CaptureTheMoment getCurrentSave() {
        CaptureTheMoment captureTheMoment = turnData.get(pointerForTurnData);
        currentPlayer = captureTheMoment == null ? null : captureTheMoment.currentPlayer;
        players = captureTheMoment == null ? null : captureTheMoment.players;
        return captureTheMoment;
    }

    public CaptureTheMoment prevSaveData() {
        pointerForTurnData--;
        return getCurrentSave();
    }

    public boolean havePrevSave() {
        return pointerForTurnData > 0;
    }

    public CaptureTheMoment nextSaveData() {
        pointerForTurnData++;
        return getCurrentSave();
    }

    public boolean haveNextSave() {
        return pointerForTurnData < turnData.size() - 1;
    }

    public boolean isPlayerInGame(String playerName) {
        for (Player player: players) {
            if (player.getName().equals(playerName))
                return true;
        }
        return false;
    }

    public void addPlayer(String playerName, Player.Type playerType) {
        players.add(new Player(playerName, (short)players.size(), playerType));
    }
}
