package UILogic;


import engine.GameEngine;
import shared.GameInfo;

import java.io.InputStream;
import java.util.HashMap;

public class GamesManager {

    private final HashMap<String, GameInfo> gamesInfoMap;
    private final HashMap<String, GameEngine> gamesMap;

    public GamesManager()
    {
        gamesInfoMap = new HashMap<>();
        gamesMap = new HashMap<>();
    }

    // return error or null if all good
    public String addNewGame(InputStream xmlFile, InputStream dictFile, String userNameFromSession)
    {
        String gameLoaded;

        synchronized (this) {
            gameLoaded = loadXML(xmlFile, dictFile,  userNameFromSession);
        }
        return gameLoaded;
    }

    public String loadXML(InputStream xmlStream, InputStream dictStream, String userNameFromSession) {
        GameInfo newGameInfo = new GameInfo();
        GameEngine gameEngine = new GameEngine();
        String res = "success";
        try {
            gameEngine.loadXml(xmlStream, dictStream, userNameFromSession);
            // gamesMap.put();
        }
        catch(Exception e){
            res =  e.getMessage();
        }
        finally {
            return res;
        }
    }

    public HashMap<String, GameInfo> getGamesInfosMap() {
        return gamesInfoMap;
    }

    public HashMap<String, GameEngine> getGamesMap() {
        return gamesMap;
    }

    public GameEngine getSpecificGame(String gameTitleToJoin)
    {
        return gamesMap.get(gameTitleToJoin);
    }

    public GameInfo getSpecificGameInfo(String gameTitleToJoin)
    {
        return gamesInfoMap.get(gameTitleToJoin);
    }
    //endregion
}
