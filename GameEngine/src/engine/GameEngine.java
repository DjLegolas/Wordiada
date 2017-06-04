package  engine;

import java.io.File;
import java.lang.String;

import engine.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import engine.Board.Point;
import engine.GameDataFromXml.*;

import engine.jaxb.schema.generated.Letter;

public class GameEngine {

    private Player currentPlayer;
    private int nextPlayerNumber = 1;
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

    public void loadXml(String pathToXml)
            throws WrongPathException, DictionaryNotFoundException, BoardSizeException, NotXmlFileException,
            DuplicateLetterException, NotValidXmlFileException, WinTypeException, NotEnoughLettersException {
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

    public void startGame() throws NumberOfPlayersException {
        currentGameData =  gdfx.get(0);
        players = new ArrayList<>();
        List<engine.jaxb.schema.generated.Player> _players = currentGameData.getPlayers();
        // TODO: check if there is a try catch outside for invalid num of players

        for (engine.jaxb.schema.generated.Player p: _players) {
                players.add(new Player(p.getName().get(0), p.getId(),p.getType()));
        }



        //IDO's code - dunno what it is


        /*
        while (players.size() < 2) {
            players.add(new Player("Player" + players.size(),));
        }*/
        currentPlayer = players.get(0);
        isGameStarted = true;
        startTime = System.currentTimeMillis();
        tryNumber = 1;
    }

    public Status getStatus() {
        GameDataFromXml gd = isGameStarted ? currentGameData : gdfx.get(gdfx.size() - 1);

        return new Status(
                gd.getBoard().getBoard_onlySigns(),
                currentPlayer != null ? currentPlayer.getName() : null,
                gd.getBoard().getKupaAmount()
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
        return currentGameData.getBoard().getBoardSize();
    }
    private boolean canRetry() {
        return tryNumber <= currentGameData.getNumOfTries();
    }

    public int getMaxRetries() {
        return currentGameData.getNumOfTries();
    }

    public WordCheck isWordValid(String word, int tries) {
        if (tries == tryNumber && canRetry()) {
            if (!currentGameData.getBoard().hasChars(word)) {
                return WordCheck.CHARS_NOT_PRESENT;
            }
            if (currentGameData.getDictionary().hasWord(word)) {
                currentGameData.getBoard().removeLettersFromBoard(word);
                currentPlayer.updateScore(word, currentGameData.calcScore(word));
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
        currentPlayer = players.get(nextPlayerNumber);
        nextPlayerNumber = (nextPlayerNumber + 1) % players.size();
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
        return players;
    }
    public Board getBoardObject(){
        return currentGameData.getBoard();}

    public Boolean isInGoldFishMod(){
        return currentGameData.getGoldFishMod();
    }

    public WinAccordingTo getWinScoreMod(){
        return currentGameData.getWinAccordingTo();
    }

    public boolean isWordScore(){
        return currentGameData.getWinAccordingTo().equals(WinAccordingTo.WORD_SCORE);
    }

    public String getFreqEachLetter(){
        StringBuilder freqLetters = new StringBuilder();
        for(Letter letter : currentGameData.getBoard().getInitLetters().keySet()) {
            freqLetters.append(letter.getSign().get(0) + " : " + letter.getFrequency()+ "\n");
        }
        return freqLetters.toString();
    }

    public String getTopTenRareWords(){
        return currentGameData.getDictionary().getTop10RareWords();
    }


}
