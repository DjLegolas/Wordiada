package UILogic;

import Generated.GameDescriptor;
import logic.AdvancedGame;
import logic.BasicGame;
import logic.Game;
import shared.GameInfo;
import shared.Validator;
import sharedStructures.eColor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class GamesManager {

    private final HashMap<String, GameInfo> m_GamesInfoMap;
    private final HashMap<String, Game> m_GamesMap;
    GameDescriptor m_GameDescriptor = new GameDescriptor();
    Validator m_Validator = new Validator();

    public GamesManager()
    {
        m_GamesInfoMap = new HashMap<>();
        m_GamesMap  = new HashMap<>();
    }

    // return error or null if all good
    public String addNewGame(InputStream file, String userNameFromSession)
    {
        String gameLoaded;

        synchronized (this) {
            gameLoaded = loadXML(file, userNameFromSession);
        }
        return gameLoaded;
    }

    public String loadXML(InputStream file, String userNameFromSession) {
        GameInfo newGameInfo = new GameInfo();
        Game game;
        String res = "success";
        try {
            m_GameDescriptor = fromXmlFileToObject(file);
            if(m_GameDescriptor != null) {
                getDataFromGeneratedXML(newGameInfo);
                newGameInfo.setOrganizer(userNameFromSession);
                game = createGame(newGameInfo);

                if(!m_GamesInfoMap.containsKey(newGameInfo.getGameTitle())) {
                    m_GamesInfoMap.put(newGameInfo.getGameTitle(), newGameInfo);
                    m_GamesMap.put(newGameInfo.getGameTitle(), game);
                }
            }
        }
        catch(Exception e){
            res =  e.getMessage();
        }
        finally {
            return res;
        }
    }

    public GameDescriptor fromXmlFileToObject(InputStream file) {
        return unmarshalFile(file);
    }

    private GameDescriptor unmarshalFile(InputStream file){
        GameDescriptor descriptor = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            descriptor = (GameDescriptor) jaxbUnmarshaller.unmarshal(file);

        } catch (JAXBException e) {
            e.printStackTrace();
           // m_StatusBar.set("Failed to load file");
        }

        return descriptor;
    }

    private void getDataFromGeneratedXML(GameInfo gameInfo) throws Exception {
        m_Validator = new Validator(gameInfo);

        gameInfo.setGameType(m_GameDescriptor.getGameType());
        gameInfo.setGameTitle(m_GameDescriptor.getDynamicPlayers().getGameTitle());
        m_Validator.checkBoardSize(m_GameDescriptor.getBoard().getSize().intValue());

        gameInfo.setBoardSize(m_GameDescriptor.getBoard().getSize().intValue());
        gameInfo.setBoardStructure(m_GameDescriptor.getBoard().getStructure().getType());

        if(gameInfo.getBoardStructure().equals("Random")) {
            gameInfo.setRangeFrom(m_GameDescriptor.getBoard().getStructure().getRange().getFrom());
            gameInfo.setRangeTo(m_GameDescriptor.getBoard().getStructure().getRange().getTo());
        }

        gameInfo.setNumOfPlayers(m_GameDescriptor.getDynamicPlayers().getTotalPlayers());
        gameInfo.setTotalPlayers(m_GameDescriptor.getDynamicPlayers().getTotalPlayers());
        gameInfo.initBoard();
        setBoardValuesFromXML(gameInfo);
    }

    private void setBoardValuesFromXML(GameInfo gameInfo) throws Exception {
        if(gameInfo.getBoardStructure().equals("Explicit")) {
            int row, col;

            List<GameDescriptor.Board.Structure.Squares.Square> squares = m_GameDescriptor.getBoard().getStructure().getSquares().getSquare();

            m_Validator.checkValidSquaresLocation(squares);

            for (GameDescriptor.Board.Structure.Squares.Square s : squares) {

                col = s.getColumn().intValue() - 1;
                row = s.getRow().intValue() - 1;
                gameInfo.setSquare(row, col, s.getValue().toString(), s.getColor());
            }

            m_Validator.checkValidMarkerLocation(m_GameDescriptor.getBoard().getStructure().getSquares().getMarker());
            GameDescriptor.Board.Structure.Squares.Marker marker = m_GameDescriptor.getBoard().getStructure().getSquares().getMarker();
            gameInfo.setSquare(marker.getRow().intValue() - 1, marker.getColumn().intValue() - 1, "@", eColor.BLACK.ordinal());
        }
        else {
            m_Validator.checkRangeForRandomBoard(gameInfo.getGameType());
        }
    }

    private Game createGame(GameInfo gameInfo) {
        GameInfo wrapper[] = new GameInfo[1];
        wrapper[0] = gameInfo;
        Game game;

        if(gameInfo.getGameType().equals("Basic")){
            game = new BasicGame(wrapper);
        }
        else{
            game = new AdvancedGame(wrapper);
        }

        return game;
    }

    public HashMap<String, GameInfo> getGamesInfosMap() {
        return m_GamesInfoMap;
    }

    public HashMap<String, Game> getGamesMap() {
        return m_GamesMap;
    }

    public Game getSpecificGame(String gameTitleToJoin)
    {
        return m_GamesMap.get(gameTitleToJoin);
    }

    public GameInfo getSpecificGameInfo(String gameTitleToJoin)
    {
        return m_GamesInfoMap.get(gameTitleToJoin);
    }
    //endregion
}
