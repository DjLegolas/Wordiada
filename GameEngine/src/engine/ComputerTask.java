package engine;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerTask extends Task<Boolean> {
    private static final short SLEEP_TIME = 200;

    private Player player;
    private GameEngine gameEngine;
    private int diceValue;

    ComputerTask(Player player, GameEngine gameEngine) {
        this.player = player;
        this.gameEngine = gameEngine;
    }

    @Override
    protected Boolean call() {
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
        List<Board.Point> selectedPoints = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < diceValue; i++) {
            updateProgress(i, diceValue);
            Board.Point point;
            do {
                point = points.get(random.nextInt(points.size()));
            } while (!selectedPoints.contains(point));
            selectedPoints.add(point);
        }
        updateMessage("Selection completed.");
        sleepForAWhile(SLEEP_TIME);
    }

    private void buildWord() {
        updateMessage("Building a word...");
        sleepForAWhile(SLEEP_TIME);
    }
}
