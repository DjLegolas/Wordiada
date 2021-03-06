package engine;

import engine.exceptions.OutOfBoardBoundariesException;
import javafx.concurrent.Task;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerTask extends Task<Boolean> {
    private static final short SLEEP_TIME = 500;

    private Player player;
    private GameEngine gameEngine;
    private int diceValue;
    private int tryNumber;
    private List<Button> currentSelectedButtons = new ArrayList<>(); // remove - No use
    private String currentSelectedWord;
    private List<int[]> selectedPoints = new ArrayList<>();


    public ComputerTask(Player player, GameEngine gameEngine) {
        this.player = player;
        this.gameEngine = gameEngine;
    }

    public List<int[]> getSelectedPoints() {
        return selectedPoints;
    }

    public void setCurrentSelectedButtons(List<Button> currentSelectedButtons) {
        this.currentSelectedButtons = currentSelectedButtons;
    }

    public List<Button> getCurrentSelectedButtons() {
        return currentSelectedButtons;
    }

    @Override
    protected Boolean call() {
        tryNumber = 1;
        getDiceValue();
        selectTilesToShow();
        GameEngine.WordCheck result;
        int maxRetries = gameEngine.getMaxRetries() + 1;
        updateProgress(0, maxRetries);
        do {
            result = buildWord();
            tryNumber++;
            updateProgress(tryNumber, maxRetries);
        } while(result == GameEngine.WordCheck.WRONG || result == GameEngine.WordCheck.CHARS_NOT_PRESENT);

        if (result == GameEngine.WordCheck.CORRECT) {
            updateMessage("The word " + currentSelectedWord + " is CORRECT!");
            sleepForAWhile(SLEEP_TIME * 2);
        }

        return result == GameEngine.WordCheck.CORRECT;
    }

    private static void sleepForAWhile(long sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {

            }
        }
    }

    private void getDiceValue() {
        updateMessage("Getting dice value...");
        sleepForAWhile(SLEEP_TIME);
        diceValue = gameEngine.getDiceValue();
        updateMessage("Dice value is: " + diceValue + ".");
        sleepForAWhile(SLEEP_TIME);
    }

    private void selectTilesToShow() {
        selectedPoints.clear();
        updateMessage("Selecting " + diceValue + " tiles to show...");
        sleepForAWhile(SLEEP_TIME);
        List<Board.Point> points = gameEngine.getUnShownPoints();
        List<int[]> selectedPoints = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < diceValue; i++) {
            updateProgress(i + 1, diceValue);
            int[] p = new int[2];
            do {
                Board.Point point = points.get(random.nextInt(points.size()));
                p[0] = point.getY();
                p[1] = point.getX();
            } while (selectedPoints.contains(p));

            selectedPoints.add(p);
        }
        this.selectedPoints = selectedPoints;
        try {
            gameEngine.updateBoard(selectedPoints);
        } catch (OutOfBoardBoundariesException e) {
            e.printStackTrace();
        }
        updateMessage("Selection completed.");
        sleepForAWhile(SLEEP_TIME);
    }

    private GameEngine.WordCheck buildWord() {
        String tryNumberStr = "Try number: "+ tryNumber + "\n";
        updateMessage(tryNumberStr +"Building a word...");
        sleepForAWhile(SLEEP_TIME);
        char[][] board = gameEngine.getBoard();
        StringBuilder word = new StringBuilder();
        List<int[]> selectedChars = new ArrayList<>();
        // build the word from the board
        for (int row = 0; row < gameEngine.getBoardSize(); row++) {
            for (int col = 0; col < gameEngine.getBoardSize(); col++) {
                char c = board[row][col];
                if (c != '\0' && c != ' ') {
                    word.append(c);
                    selectedChars.add(new int[]{row + 1, col + 1});
                }
                if (word.length() >= 2) {
                    break;
                }
            }
            if (word.length() >= 2) {
                break;
            }
        }
        currentSelectedWord = word.toString();
        selectedPoints = selectedChars;
        updateMessage(tryNumberStr + "The word is \"" + word + "\"");
        sleepForAWhile(SLEEP_TIME);
        return gameEngine.isWordValidWithCoordinates(word.toString(), tryNumber, selectedChars);
    }

}
