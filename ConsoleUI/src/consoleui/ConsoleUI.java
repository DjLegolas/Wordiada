package consoleui;

import java.util.List;

public class ConsoleUI {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        int selectedMenu;
        boolean needInput = true;
        while((selectedMenu= ConsoleHandler.showMainMenu()) != 6){
            switch (selectedMenu) {
                case 1:
                    String pathToXml = null;
                    while (needInput) {
                        pathToXml = ConsoleHandler.getXML();
                        try {
                            engine.loadXml(pathToXml);
                            needInput = false;
                        } catch (Exception e) {
                            System.out.println("Error, " + e.getMessage());
                        }
                    }
                    System.out.println("XML file " + pathToXml + " loaded successfully!");
                    needInput = true;
                    break;
                case 2:
                    if (!engine.isXmlLoaded()) {
                        System.out.println("No xml game file was loaded.\n" +
                                "Please select 1 first to load at least one xml file.");
                    }
                    else if (engine.isStarted()) {
                        System.out.println("The game was already started...\n" +
                                "Please DON'T use this option again.");
                    }
                    else {
                        engine.startGame();
                    }
                    break;
                case 3:
                    ConsoleHandler.showGameStatus(engine.getStat());
                    break;
                case 4:
                    int diceValue = engine.getDiceValue(), tries;
                    List<int[]> points = ConsoleHandler.getPoints(diceValue);
                    engine.updateBoard(points);
                    Object o = engine.getBoard();
                    ConsoleHandler.printBoard(5, 7, o);
                    int maxTries = engine.getRetriesAmount();
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
                    ConsoleHandler.showGameStatus(engine.getStat());
                    break;
                case 5:
                    Object ob = engine.getStatistics();
                    ConsoleHandler.showStatistics(ob);
                    break;
            }
        }

        System.out.println("Game ended.");
        Object o = engine.getBoard();
        ConsoleHandler.printBoard(5, 7, o);
        Object ob = engine.getStatistics();
        ConsoleHandler.showStatistics(ob);
    }
}
