package  engine;

import java.io.File;
import java.lang.String;

import engine.exceptions.*;

import java.util.*;

import engine.Board.Point;
import engine.GameDataFromXml.*;

import engine.jaxb.schema.generated.Letter;
import javafx.scene.control.Alert;
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
    public enum WordCheck {
            CORRECT, WRONG, WRONG_CANT_RETRY, CHARS_NOT_PRESENT, TRIES_DEPLETED
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


        //IDO's code - dunno what it is


        /*
        while (players.size() < 2) {
            players.add(new Player("Player" + players.size(),));
        }*/
        currentPlayer = players.get(0);
        startTime = System.currentTimeMillis();
        tryNumber = 1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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
        return tryNumber <= currentGameData.getNumOfTries();
    }

    public int getMaxRetries() {
        GameDataFromXml gameDataFromXml = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);
        return gameDataFromXml.getNumOfTries();
    }


    public WordCheck isWordValid(String word, int tries) {
        if (tries == tryNumber && canRetry()) {
            if (!currentGameData.getBoard().hasChars(word)) {
                tryNumber++;
                return WordCheck.CHARS_NOT_PRESENT;
            }
            Dictionary.Word dictWord = currentGameData.getDictionary().hasWord(word);
            if (dictWord != null) {
                currentGameData.getBoard().removeLettersFromBoard(word);
                currentPlayer.updateScore(dictWord, currentGameData.calcScore(word));
                tryNumber = 1;
                return WordCheck.CORRECT;
            }
            tryNumber++;
            if (!canRetry()) {
                nextPlayer();
                return WordCheck.WRONG_CANT_RETRY;
            }
            return WordCheck.WRONG;
        }
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
        return new Statistics(players, System.currentTimeMillis() - startTime, numberOfTurns, currentGameData);
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

    public boolean isGameEnded(){
        //if no more left cards in kupa and all the tails in the board are shown
        return (currentGameData.getBoard().getKupaAmount() == 0) || (getUnShownPoints().isEmpty());
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
        return isGameStarted ? currentPlayer.getId() : 0;
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


    public Map<String, Pair<Integer, Integer>> getPlayerWords() {
        Map<String, Pair<Integer, Integer>> words = new HashMap<>();
        for (Map.Entry<Dictionary.Word, Integer> entry: currentPlayer.getWords().entrySet()) {
            words.put(entry.getKey().getWord(), new Pair<>(entry.getValue(), entry.getKey().getScore()));
        }
        return words;
    }

    public boolean retirePlayer() {
        if (numOfPlayers == 2) {
            return false;
        }
        currentPlayer.retire();
        nextPlayer();
        return true;
    }
}
