public class ConsoleUI {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        int selectedMenu;
        while((selectedMenu=MenuHandler.showMainMenu()) != 6){
            switch (selectedMenu){
                case 1:
                    String pathToXml = MenuHandler.getXML();
                    try {
                        engine.loadXml(pathToXml);
                    }
                    catch (Exception e) {
                        System.out.println("Error, " + e.getMessage());
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }
    }
}
