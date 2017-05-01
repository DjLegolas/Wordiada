package consoleui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import engine.Statistics;
import engine.Status;

class ConsoleHandler {

    static int showMainMenu() {
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

    static String getXML() {
        Scanner scanner = new Scanner(System.in);
        String pathToXml;

        System.out.println("Please enter the path to the XML file:");
        return scanner.nextLine();
    }

    static void showGameStatus(Status status, boolean needFullPrint) {
        System.out.println("Game Status:\n");
        printBoard(status.getBoard());
        String line = "The number of cards ";
        if (needFullPrint) {
            line += "remaining ";
        }
        line += "in the pot: " + status.getLeftTiles();
        System.out.println(line);
        if (needFullPrint) {
            System.out.println("Current player: " + status.getPlayerName());
        }
    }

    static void printBoard(char[][] board) {
        // TODO: change the signature of the function and fill with correct data
        int numOfRows = board.length;
        int numOfCols = board.length;
        int col, row;
        StringBuilder line = new StringBuilder();
        StringBuilder boarderLine = new StringBuilder(numOfRows > 9 ? "-----" : "---");
        String tmpStr;
        for (col = 0; col < numOfCols; col++) {
            boarderLine.append("--");
        }
        System.out.println("Current Board:\n");
        boardIndices(numOfCols, numOfRows);
        System.out.println(boarderLine);
        // print board
        for (row = 0; row < numOfRows; row++) {
            line = new StringBuilder(numOfRows > 9 && row < 9 ? " " : "");
            tmpStr = (row + 1) + "|";
            line.append(tmpStr);
            for (col = 0; col < numOfCols; col++) {
                tmpStr = board[row][col] + "|";
                line.append(tmpStr);
            }
            line.append(row + 1);
            System.out.println(line);
            System.out.println(boarderLine);
        }
        boardIndices(numOfCols, numOfRows);
    }

    private static void boardIndices(int numOfCols, int numOfRows) {
        int col;
        StringBuilder line = new StringBuilder();
        String tmpStr, lineStart = numOfRows > 9 ? "  |" : " |";
        line.append(lineStart);
        if (numOfRows > 9) {
            for (col = 1; col <= numOfCols; col++) {
                line.append(col % 10 == 0 ? col / 10 + "|" : " |");
            }
            System.out.println(line);
        }
        line = new StringBuilder(lineStart);
        for (col = 0; col < numOfCols; col++) {
            tmpStr = (col + 1) % 10 + "|";
            line.append(tmpStr);
        }
        System.out.println(line);
    }

    static List<int[]> getPoints(int numOfValues, boolean sizeWasTooShort) {
        Scanner scanner = new Scanner(System.in);
        List<int[]> points = new ArrayList<>();
        if (sizeWasTooShort) {
            System.out.println("The number of coordinates is too short! Try again...\n");
        }
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

    static String getWord(int tryNum, int maxTries) {
        String word;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Try #" + tryNum + " out of " + maxTries + ":");
        System.out.println("Please enter a word built from the letters above:");
        word = scanner.next();
        return word.toUpperCase();
    }

    static void showStatistics(Statistics stats) {
        System.out.println("Game Statistics:\n");
        // number of turns
        System.out.println("Turns played: " + stats.getNumOfTurns());
        // total time played
        long time = stats.getTime();
        System.out.println("Time passed from game start: " + time / 60 + ":" + time % 60);
        // cards left
        int cardsLeft = stats.getLeftBoxTiles();
        System.out.println("Number of cards left: " + cardsLeft);
        for (int i = 0; i < 12; i++) {
            //TODO: fix for
            System.out.println("\t" + ('A' + i) + " - " + 4 + "/" + 30);
        }
        // players
        long totalWords = stats.getTotalWords();
        for (Statistics.PlayerData player: stats.getPlayers()) {
            System.out.println("Player " + player.getName());
            System.out.println(" Score: " + player.getScore());
            System.out.println(" Words:");
            for (String word: player.getWords()) {
                System.out.println("\t" + word + ": " + stats.getWordCount(word) + "/" + totalWords);
            }
        }
    }
}
