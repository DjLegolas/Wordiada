package consoleui;

import java.util.List;
import engine.GameEngine;
import engine.Statistics;
import engine.exceptions.*;

public class ConsoleUI {
    private static GameEngine engine = new GameEngine();
    public static void main(String[] args) {
        int selectedMenu;
        while((selectedMenu = ConsoleHandler.showMainMenu()) != 6){
            switch (selectedMenu) {
                case 1:
                    getXml();
                    break;
                case 2:
                    startGame();
                    break;
                case 3:
                    if (isGameStarted()) {
                        ConsoleHandler.showGameStatus(engine.getStatus(), true);
                    }
                    break;
                case 4:
                    if (isGameStarted()) {
                        playTurn();
                    }
                    break;
                case 5:
                    if (isGameStarted()) {
                        Statistics stat = engine.getStatistics();
                        ConsoleHandler.showStatistics(stat);
                    }
                    break;
                default:
                    System.out.println("Wrong menu number (need to bo 1-6).");
                    break;
            }
        }

        System.out.println("---- Game ended ----");
        if (engine.isStarted()) {
            char[][] board = engine.getBoard();
            ConsoleHandler.printBoard(board);
            Statistics stat = engine.getStatistics();
            ConsoleHandler.showStatistics(stat);
        }
    }

    private static void getXml() {
        if (engine.isStarted()) {
            System.out.println("The game was already started...\n" +
                    "Unable to load more XML files.\n");
            return;
        }

        boolean needInput = true;
        String pathToXml = null;
        String message = "";
        while (needInput) {
            pathToXml = ConsoleHandler.getXML();
            try {
                engine.loadXml(pathToXml);
                needInput = false;
            } catch (WrongPathException e) {
                message = "Invalid path to XML file.";
            } catch (NotXmlFileException e) {
                message = "The file \"" + pathToXml + "\" is not an XML file.";
            } catch (DictionaryNotFoundException e) {
                message = "Unable to use dictionary file \"" + e.getFileName() + "\"";
            } catch (DuplicateLetterException e) {
                message = "The letter " + e.getLetter() + " appears more than once.";
            } catch (BoardSizeException e) {
                message = "Expected size is between " + e.getMinSize() + " to " + e.getMaxSize() + ". Got: " + e.getSize();
            } catch (NotValidXmlFileException e) {
                message = "The XML file \"" + pathToXml + "\"\n" +
                        "does not contains the information for Wordiada game.";
            } catch (WinTypeException e) {
                message = "Winning type \"" + e.getWinType() + "\" is not currently supported.";
            } catch (NotEnoughLettersException e) {
                message = "There of cards is not enough to fill the board.\n" +
                    "The board needs " + e.getExpectedAmount() + " but there are only " + e.getCurrentAmount();
            } catch (Exception e) {
                message = "Error, " + e.getMessage();
            }
            if (needInput) {
                ConsoleHandler.printError("XML load failed", message);
            }
        }
        ConsoleHandler.showGameStatus(engine.getStatus(), false);
        System.out.println("XML file \"" + pathToXml + "\" loaded successfully!\n");
    }

    private static void startGame(){
        String errTitle = "Starting game failed";
        if (!engine.isXmlLoaded()) {
            ConsoleHandler.printError(errTitle, "No xml game file was loaded.\n" +
                    "Please select 1 first to load at least one xml file.\n");
        }
        else if (engine.isStarted()) {
            ConsoleHandler.printError(errTitle, "The game was already started...\n" +
                    "Please DON'T use this option again.\n");
        }
        else {
            try {
                engine.startGame();
            } catch (NumberOfPlayersException e) {
                ConsoleHandler.printError(errTitle, "The number of players is incorrect.\n" +
                        "The minimum is " + e.getMinPlayers() + ", the maximum is " + e.getMaxPlayers() +
                        ", but got " + e.getActualNumOfPlayers() + " players.\n");
                return;
            }
            System.out.println("Game started! Have fun :>\n");
        }
    }

    private static void playTurn() {
        boolean listSizeTooShort = false, continueTrying = true, outOfBoarder;
        List<int[]> points;
        int diceValue = engine.getDiceValue(), tryNumber;
        System.out.println("Dice value is " + diceValue);
        do {
            outOfBoarder = false;
            points = ConsoleHandler.getPoints(diceValue, listSizeTooShort);
            try {
                listSizeTooShort = !engine.updateBoard(points);
            } catch (OutOfBoardBoundariesException e) {
                ConsoleHandler.printError("Error", "Some of the points you chose are out of boundaries!\n Try again.");
                outOfBoarder = true;
            }
        } while(listSizeTooShort && !outOfBoarder);
        char[][] board = engine.getBoard();
        ConsoleHandler.printBoard(board);
        int maxTries = engine.getMaxRetries();
        for (tryNumber = 1; continueTrying && tryNumber <= maxTries; tryNumber++) {
            String word = ConsoleHandler.getWord(tryNumber, maxTries);
            switch (engine.isWordValid(word, tryNumber)) {
                case CORRECT:
                    System.out.println("The word " + word + " is correct!\n");
                    continueTrying = false;
                    break;
                case WRONG:
                    System.out.println("Incorrect word!\n");
                    break;
                case TRIES_DEPLETED:
                    System.out.println("\nNo more retries!");
                    continueTrying = false;
                    break;
                case CHARS_NOT_PRESENT:
                    System.out.println("You wrote a word with unavailable characters...");
                    System.out.println("Please try again...\n");
                    tryNumber--;
                    break;
                case WRONG_CANT_RETRY:
                    System.out.println("Incorrect word!\nNo more retries!");
                    System.out.println("Changing to next player...");
                    continueTrying = false;
                    break;
            }
        }
        ConsoleHandler.showGameStatus(engine.getStatus(), true);
    }

    private static boolean isGameStarted() {
        if (engine.isStarted()) {
            return true;
        }
        System.out.println("The game wasn't started.\n" +
                "Please select option 2 to start it" +
                (!engine.isXmlLoaded() ? ", after loading at least one xml" : "") +
                ".\n");
        return false;
    }
}
