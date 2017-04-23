package consoleui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleHandler {

    public static int showMainMenu() {
        int selectedMenuItem;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please select an option:");
        System.out.println("1. Load game from xml.");
        System.out.println("2. Start game.");
        System.out.println("3. Show board.");
        System.out.println("4. Make a move.");
        System.out.println("5. Show statistics.");
        System.out.println("6. Exit game.");

        selectedMenuItem = scanner.nextInt();
        if (selectedMenuItem < 1 && selectedMenuItem > 6) {
            System.out.println("Wrong menu number (need to bo 1-6).");
        }

        return selectedMenuItem;
    }

    public static String getXML() {
        Scanner scanner = new Scanner(System.in);
        String pathToXml;

        System.out.println("Please enter the path to the XML file:");
        pathToXml = scanner.nextLine();
        if (!pathToXml.toLowerCase().endsWith(".xml")) {
            System.out.println("The path entered is not to a XML file!");
        }

        return pathToXml;
    }

    public static void showGameStatus(Object o) {
        // TODO: change 'Object o' to the correct type
        System.out.println("Game Stats:\n");
        printBoard(5,5, null);
        System.out.println("The number of cards remaining in the pot: " + 5);
        System.out.println("Current player: " + "Player1");
    }

    public static void printBoard(int x, int y, Object o) {
        // TODO: change the signature of the function and fill with correct data
        int numOfRows = y;
        int numOfCols = x;
        int col, row;
        String line;
        String boarderLine = numOfRows > 9 ? "-----" : "---";
        for (col = 0; col < numOfCols; col++) {
            boarderLine += "--";
        }
        System.out.println("Current Board:\n");
        boardIndices(numOfCols, numOfRows);
        System.out.println(boarderLine);
        // print board
        for (row = 0; row < numOfRows; row++) {
            line = numOfRows > 9 && row < 9 ? " " : "";
            line += (row + 1) + "|";
            for (col = 0; col < numOfCols; col++) {
                line += " |";
            }
            line += row + 1;
            System.out.println(line);
            System.out.println(boarderLine);
        }
        boardIndices(numOfCols, numOfRows);
    }

    private static void boardIndices(int numOfCols, int numOfRows) {
        int col;
        String line;
        String lineStart = numOfRows > 9 ? "  |" : " |";
        // Print top indices
        line = lineStart;
        if (numOfRows > 9) {
            for (col = 1; col <= numOfCols; col++) {
                line += col % 10 == 0 ? col / 10 + "|" : " |";
            }
            System.out.println(line);
        }
        line = lineStart;
        for (col = 0; col < numOfCols; col++) {
            line += (col + 1) % 10 + "|";
        }
        System.out.println(line);
    }

    public static List<int[]> getPoints(int numOfValues) {
        Scanner scanner = new Scanner(System.in);
        List<int[]> points = new ArrayList<>();
        System.out.println("Please enter "+ numOfValues + "coordinates.");
        System.out.println("The format is: row col. example: 5 5\n");
        for (int i = 0; i < numOfValues; i++) {
            System.out.println();
            int point[] = {-1,-1};
            point[0] = scanner.nextInt();
            point[1] = scanner.nextInt();
            points.add(point);
        }
        return points;
    }

    public static String getWord(int tryNum, int maxTries) {
        String word;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Try #" + tryNum + " out of " + maxTries + ":");
        System.out.println("Please enter a word built from the letters above:");
        word = scanner.next();
        return word.toUpperCase();
    }

    public static void showStatistics(Object o) {
        //TODO: fix object and data
        System.out.println("Game Statistics:\n");
        System.out.println("Turns played: " + 12);
        System.out.println("Time passed from game start: " + "12:40");
        System.out.println("Number of cards left: " + 30);
        for (int i = 0; i < 12; i++) {
            //TODO: fix for
            System.out.println("\t" + ('A' + i) + " - " + 4 + "/" + 30);
        }
        for (int i = 0; i < 2; i++) {
            System.out.println("Player " + "Ido");
            System.out.println(" Score: " + 5000);
            for (int j = 0; j < 3; j++) {
                System.out.println("umbrella" + ": " + 34 + "/" + 12398);
            }
        }
    }
}
