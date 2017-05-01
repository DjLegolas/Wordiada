package consoleui;

import java.util.List;
import engine.GameEngine;
import engine.Statistics;
import engine.exceptions.*;

public class ConsoleUI {
    // TODO: fix exceptions
    private static GameEngine engine = new GameEngine();
    public static void main(String[] args) {
        int selectedMenu;
        while((selectedMenu= ConsoleHandler.showMainMenu()) != 6){
            // TODO: handle unloaded xml when choosing options 3-6
            switch (selectedMenu) {
                case 1:
                    getXml();
                    break;
                case 2:
                    startGame();
                    break;
                case 3:
                    ConsoleHandler.showGameStatus(engine.getStatus(), true);
                    break;
                case 4:
                    playTurn();
                    break;
                case 5:
                    Statistics stat = engine.getStatistics();
                    ConsoleHandler.showStatistics(stat);
                    break;
            }
        }

        System.out.println("---- Game ended ----");
        char[][] board = engine.getBoard();
        ConsoleHandler.printBoard(board);
        Statistics stat = engine.getStatistics();
        ConsoleHandler.showStatistics(stat);
    }

    private static void getXml() {
        boolean needInput = true;
        String pathToXml = null;
        while (needInput) {
            pathToXml = ConsoleHandler.getXML();
            try {
                engine.loadXml(pathToXml);
                needInput = false;
            } catch (WrongPathException e) {
                System.out.println("Invalid path to XML file.\n");
            } catch (NotXmlFileException e) {
                System.out.println("The file \"" + pathToXml + "\" is not an XML file.\n");
            } catch (DictionaryNotFoundException e) {
                System.out.println("Unable to use dictionary file \"" + e.getFileName() + "\"\n");
            } catch (DuplicateLetterException e) {
                System.out.println("The letter " + e.getLetter() + " appears more than once.\n");
            } catch (BoardSizeException e) {
                System.out.println("XML not valid!");
                System.out.println("Expected size is between " + e.getMinSize() + " to " + e.getMaxSize());
                System.out.println("Got: " + e.getSize());
            } catch (NotValidXmlFileException e) {
                System.out.println("The XML file \"" + pathToXml +"\"\n" +
                        "does not contains the information for Wordiada game.\n");
            } catch (Exception e) {
                System.out.println("Error, " + e.getMessage());
            }
        }
        ConsoleHandler.showGameStatus(engine.getStatus(), false);
        System.out.println("XML file \"" + pathToXml + "\" loaded successfully!\n");
    }

    private static void startGame(){
        if (!engine.isXmlLoaded()) {
            System.out.println("No xml game file was loaded.\n" +
                    "Please select 1 first to load at least one xml file.\n");
        }
        else if (engine.isStarted()) {
            System.out.println("The game was already started...\n" +
                    "Please DON'T use this option again.\n");
        }
        else {
            try {
                engine.startGame();
            } catch (NumberOfPlayersException e) {
                System.out.println("The number of players is incorrect.\n" +
                        "The minimum is " + e.getMinPlayers() + ", the maximum is " + e.getMaxPlayers() +
                        ", but got " + e.getActualNumOfPlayers() + " players.\n");
            }
        }
    }

    private static void playTurn() {
        boolean listSizeTooShort = false;
        List<int[]> points;
        int diceValue = engine.getDiceValue(), tries;
        do {
            points = ConsoleHandler.getPoints(diceValue, listSizeTooShort);
            listSizeTooShort = !engine.updateBoard(points);
        } while(listSizeTooShort);
        char[][] board = engine.getBoard();
        ConsoleHandler.printBoard(board);
        int maxTries = engine.getMaxRetries();
        for (tries = 0; tries < maxTries; tries++) {
            String word = ConsoleHandler.getWord(tries + 1, maxTries);
            if (engine.isWordValid(word, tries)) {
                System.out.println("The word " + word + " is correct!\n");
                break;
            }
            System.out.println("Incorrect word!\n");
        }
        if (tries == maxTries) {
            System.out.println("\nNo more retries!");
        }
        System.out.println("Changing to next player...");
        ConsoleHandler.showGameStatus(engine.getStatus(), true);
    }
}
