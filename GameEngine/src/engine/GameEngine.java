package  engine;

import java.lang.String;
import engine.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {

    // private GameInformation info;
    private Player currentPlayer;
    private int nextPlayerNumber = 1;
    private List<Player> players;
    private List<GameDataFromXml> gdfx = new ArrayList<>();
    private GameDataFromXml currentGameData;
    private boolean isGameStarted = false;
    private int diceValue;

    public void loadXml(String pathToXml)
            throws WrongPathException, DictionaryNotFoundException, BoardSizeException, NotXmlFileException {
        GameDataFromXml gd = new GameDataFromXml();
        gd.initializingDataFromXml(pathToXml);
        //check validation:
        gd.isValidXml(pathToXml);
        gd.isDictionaryInRightPos(pathToXml);
        gd.isValidBoardSize(gd.getBoardSize());
        try{
            gd.isAllLettersApperOne();
        }
        catch(InvalidInputException e){
            throw new DictionaryNotFoundException(gd.getDictFileName());
        }
        gdfx.add(gd);
    }


    public boolean isXmlLoaded() {
        return !gdfx.isEmpty();
    }

    public boolean isStarted() {
        return isGameStarted;
    }

    public void startGame() throws BoardSizeException {
        currentGameData =  gdfx.get(0);
        players = new ArrayList<>();
        /*for (engine.jaxb.schema.generated.Player p: currentGameData.getPlayers()) { //TODO: add function
            players.add(new Player(p.getName().get(0)));
        }*/
        currentPlayer = players.get(0);
        isGameStarted = true;
    }

    public Status getStatus() {
        if (currentGameData == null) {
            GameDataFromXml gd = gdfx.get(gdfx.size() - 1);
            return new Status(gd.getBoard().getBoard(),
                    currentPlayer != null ? currentPlayer.getName() : null,
                    gd.getNumOfTries());
        }
        return new Status(
                currentGameData.getBoard().getBoard(),
                currentPlayer != null ? currentPlayer.getName() : null,
                currentGameData.getNumOfTries());
        //return new Status(new char[4][4], currentPlayer.getName(), currentGameData.getNumOfTries());
    }

    public int getDiceValue() {
        Random random = new Random();
        diceValue = random.nextInt(currentGameData.getNumOfCubeWigs() - 1) + 2;
        return diceValue;
    }

    public boolean updateBoard(List<int[]> points) {
        if (points.size() > diceValue) {
            return false;
        }
        try {
            currentGameData.updateBoard(points);
            return true;
        }
        catch (OutOfBoardBoundariesException e){
            System.out.println("Some of the points you chose are out of boandries!\n Try again.");
            return false;
        }
    }

    public char[][] getBoard() {
        //TODO: uncomment
        //return board.getBoard();
        return new char[4][4];

    }

    public int getMaxRetries() {
        return currentGameData.getNumOfTries();

    }

    public boolean isWordValid(String word, int tries) {
        if (tries <= currentGameData.getNumOfTries()) {
            if (true) { //TODO: check in dictionary
                //TODO: update user
                int score = 0;
                // TODO: calculate score
                currentPlayer.updateScore(word, score);
                nextPlayer();
                return true;
            }
            return false;
        }
        nextPlayer();
        return false; //TODO: throw NumOfRetriesException
    }

    private void nextPlayer() {
        currentPlayer = players.get(nextPlayerNumber);
        nextPlayerNumber = (nextPlayerNumber + 1) & players.size();
    }

    public Statistics getStatistics() {
        return new Statistics(null, System.currentTimeMillis(), 0, 0, null, null);
    }
}
