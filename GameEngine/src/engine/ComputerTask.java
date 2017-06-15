package engine;

import engine.exceptions.OutOfBoardBoundariesException;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerTask extends Task<Boolean> {
    private static final short SLEEP_TIME = 200;

    private Player player;
    private GameEngine gameEngine;
    private int diceValue;
    private int tryNumber;

    ComputerTask(Player player, GameEngine gameEngine) {
        this.player = player;
        this.gameEngine = gameEngine;
    }

    @Override
    protected Boolean call() {
        tryNumber = 0;
        getDiceValue();
        selectTilesToShow();
        buildWord();

        return null;
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
        updateMessage("Selecting " + diceValue + " tiles to show...");
        sleepForAWhile(SLEEP_TIME);
        List<Board.Point> points = gameEngine.getUnShownPoints();
        List<int[]> selectedPoints = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < diceValue; i++) {
            updateProgress(i, diceValue);
            int[] p = new int[2];
            do {
                Board.Point point = points.get(random.nextInt(points.size()));
                p[0] = point.getY();
                p[1] = point.getX();
            } while (!selectedPoints.contains(p));
            selectedPoints.add(p);
        }
        try {
            gameEngine.updateBoard(selectedPoints);
        } catch (OutOfBoardBoundariesException e) {
            e.printStackTrace();
        }
        updateMessage("Selection completed.");
        sleepForAWhile(SLEEP_TIME);
    }

    private void buildWord() {
        String tryNumberStr = "Try number: "+ tryNumber + "\n";
        updateMessage(tryNumberStr +"Building a word...");
        sleepForAWhile(SLEEP_TIME);
        char[][] board = gameEngine.getBoard();
        StringBuilder word = new StringBuilder();
        // build the word from the board
        for (char[] row: board) {
            for (char c: row) {
                if (c != '\0' && c != ' ') {
                    word.append(c);
                }
                if (word.length() >= 2) {
                    break;
                }
            }
            if (word.length() >= 2) {
                break;
            }
        }
        updateMessage(tryNumberStr + "The word is \"" + word + "\"");
        sleepForAWhile(SLEEP_TIME);
        gameEngine.isWordValid(word.toString(), tryNumber);
    }
}
