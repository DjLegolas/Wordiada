package  engine;

import java.io.File;
import java.lang.String;

import engine.exceptions.*;

import java.util.*;

import engine.Board.Point;
import engine.GameDataFromXml.*;

import engine.jaxb.schema.generated.Letter;
import javafx.scene.control.Button;
import javafx.util.Pair;

public class GameEngine {

    private Player currentPlayer;
    private int nextPlayerNumber = 1;
    private int numOfPlayers;
    private List<Player> players;
    private List<GameDataFromXml> gdfx = new ArrayList<>();
    private GameDataFromXml currentGameData;
    private boolean isGameStarted = false;
    private int diceValue;
    private long startTime;
    private int numberOfTurns = 0;
    private int tryNumber;
    private Map <Integer, CaptureTheMoment> turnData = new HashMap<>();
    private int pointerForTurnData = -1;
    public enum WordCheck {
            CORRECT, WRONG, WRONG_CANT_RETRY, CHARS_NOT_PRESENT, TRIES_DEPLETED
    }


    //TODO: we need also save dice value , num of tries?
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
            NumberOfPlayersException, DuplicatePlayerIdException {
        GameDataFromXml gd = new GameDataFromXml();
        gd.initializeDataFromXml(pathToXml);
        gdfx.add(gd);
    }

    public void loadXml(File file)
            throws WrongPathException, DictionaryNotFoundException, BoardSizeException, NotXmlFileException,
            DuplicateLetterException, NotValidXmlFileException, WinTypeException, NotEnoughLettersException {
        GameDataFromXml gd = new GameDataFromXml();
        gd.initializeDataFromXml(file);
        gdfx.add(gd);
    }

    public void reset() {
        currentGameData.resetBoard();
    }

    public boolean isXmlLoaded() {
        return !gdfx.isEmpty();
    }

    public boolean isStarted() {
        return isGameStarted;
    }

    private List<Player> getPlayersList() {
        List<Player> players = new ArrayList<>();
        GameDataFromXml gd = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
        List<engine.jaxb.schema.generated.Player> _players = gd.getPlayers();

        for (engine.jaxb.schema.generated.Player p: _players) {
            players.add(new Player(p.getName().get(0), p.getId(),p.getType()));
        }
        return players;
    }

    public void startGame() {
        isGameStarted = true;
        currentGameData =  gdfx.get(gdfx.size() - 1);
        players = getPlayersList();
        numOfPlayers = players.size();
        currentPlayer = players.get(0);
        tryNumber = 1;
        numberOfTurns = 0;
        pointerForTurnData = -1;
        turnData.clear();
        startTime = System.currentTimeMillis();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isPlayerComputer() {
        return currentPlayer.isComputer();
    }

    public Status getStatus() {
        GameDataFromXml gd = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);

        return new Status(
                gd.getBoard().getBoard_onlySigns(),
                currentPlayer != null ? currentPlayer.getName() : null,
                gd.getBoard().getKupaAmount(),
                currentPlayer != null ? currentPlayer.getScore() : 0,
                currentPlayer != null ? currentPlayer.getId() : 0
        );
    }

    public int getDiceValue() {
        Random random = new Random();
        diceValue = random.nextInt(currentGameData.getNumOfCubeWigs() - 1) + 2;
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
        return currentGameData.getBoard().getBoard_onlySigns();
    }

    public short getBoardSize(){
        GameDataFromXml gameDataFromXml = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
        return gameDataFromXml.getBoard().getBoardSize();
    }

    private boolean canRetry() {
        return tryNumber <= currentGameData.getNumOfTries() + 1;
    }

    public int getMaxRetries() {
        GameDataFromXml gameDataFromXml = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
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
    }

    public Statistics getStatistics() {
        return new Statistics(players, System.currentTimeMillis() - startTime, numberOfTurns + 1, currentGameData);
    }

    public List<Board.Point> getUnShownPoints(){
            List<Board.Point> unShownPoints = new ArrayList<>();
            for(int row = 0; row <currentGameData.getBoard().getBoardSize(); row++){
                for(int col = 0; col < currentGameData.getBoard().getBoardSize(); col++){
                    if(!(currentGameData.getBoard().getBoardFullDetails()[row][col].isShown)) {
                        Point p = new Board.Point (row+1,col+1);
                        unShownPoints.add(p);
                    }
                }
            }
            return unShownPoints;
    }

    public boolean isGameEnded() {
        //if no more left cards in kupa and all the tails in the board are shown
        if ((numOfPlayers < 2 || currentGameData.getBoard().getKupaAmount() == 0) || (getUnShownPoints().isEmpty())) {
            isGameStarted = false;
            return true;
        }
        return false;
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
        GameDataFromXml gameDataFromXml = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
        return gameDataFromXml.getGoldFishMod();
    }

    public WinAccordingTo getWinScoreMod(){
        GameDataFromXml gameDataFromXml = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
        return gameDataFromXml.getWinAccordingTo();
    }

    public boolean isWordScore(){
        GameDataFromXml gameDataFromXml = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
        return gameDataFromXml.getWinAccordingTo().equals(WinAccordingTo.WORD_SCORE);
    }

    public String getFreqEachLetter(){
        GameDataFromXml gameDataFromXml = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
        StringBuilder freqLetters = new StringBuilder();
        for(Letter letter : gameDataFromXml.getBoard().getInitLetters().keySet()) {
            freqLetters.append(letter.getSign().get(0) + " : " + letter.getFrequency()+ "\n");
        }
        return freqLetters.toString();
    }

    public String getTopTenRareWords(){
        GameDataFromXml gameDataFromXml = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
        return gameDataFromXml.getDictionary().getTop10RareWords();
    }
    public GameDataFromXml getCurrentGameData(){
        return currentGameData;
    }


    public Map<String, Pair<Integer, Integer>> getPlayerWords(int playerId) {
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

    public boolean retirePlayer() {
        currentPlayer.retire();
        saveTheTurn(new ArrayList<>());
        numOfPlayers--;
        if (numOfPlayers < 2) {
            isGameStarted = false;
            return false;
        }
        nextPlayer();
        return true;
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
}
