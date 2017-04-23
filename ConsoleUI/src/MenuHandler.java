import java.util.Scanner;

public class MenuHandler {

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
}
