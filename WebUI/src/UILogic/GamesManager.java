package UILogic;


import engine.ComputerTask;
import engine.GameEngine;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GamesManager {

    private final Map<String, GameEngine> gamesMap;
    private final Map<String, ComputerTask> computersMap;

    public GamesManager()
    {
        gamesMap = new HashMap<>();
        computersMap = new HashMap<>();
    }

    // return error or null if all good
    public String addNewGame(InputStream xmlFile, InputStream dictFile, String dictFileName, String userNameFromSession)
    {
        String gameLoaded;

        synchronized (this) {
            gameLoaded = loadXML(xmlFile, dictFile, dictFileName,  userNameFromSession);
        }
        return gameLoaded;
    }

    private String loadXML(InputStream xmlStream, InputStream dictStream, String dictFileName, String userNameFromSession) {
        GameEngine gameEngine = new GameEngine();
        String res = "success";
        try {
            gameEngine.loadXml(xmlStream, dictStream, dictFileName, userNameFromSession);
            gamesMap.put(gameEngine.getGameTitle(), gameEngine);
            computersMap.put(gameEngine.getGameTitle(), null);
        }
        catch(Exception e){
            res =  e.getMessage();
        }
        return res;
    }

    public Map<String, ComputerTask> getComputersMap() {
        return computersMap;
    }

    public Map<String, GameEngine> getGamesMap() {
        return gamesMap;
    }

    public GameEngine getSpecificGame(String gameTitleToJoin)
    {
        return gamesMap.get(gameTitleToJoin);
    }

    public ComputerTask getSpecificComputer(String gameTitleToJoin)
    {
        return computersMap.get(gameTitleToJoin);
    }
}
