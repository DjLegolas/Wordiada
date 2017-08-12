
package Servelet;

import UILogic.GamesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engine.*;
import engine.Dictionary;
import engine.exceptions.OutOfBoardBoundariesException;
import engine.PlayerData;
import Utils.Constants;
import Utils.ServletUtils;
import Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@WebServlet(name = "GameRoomServlet", urlPatterns = {"/gamingRoom"})
public class GameRoomServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String action = request.getParameter(Constants.ACTION_TYPE);
        switch (action) {
            case Constants.GAME_STATUS:
                gameStatus(request, response);
                break;
            case Constants.IS_GAME_STARTED:
                isGameStarted(request, response);
                break;
            case Constants.EXIT_GAME:
                exitGame(request, response);
                break;
            case Constants.GET_BOARD:
                getBoard(request, response);
                break;
            case Constants.GET_SHOW_BOARD:
                getShowBoard(request, response);
                break;
            case Constants.IS_GAME_DONE:
                isGameDone(request,response);
                break;
            case Constants.THROW_DICE:
                throwDice(request,response);
                break;
            case Constants.UPDATE_BOARD:
                updateBoard(request,response);
                break;
            case Constants.CHECK_WORD:
                checkWord(request, response);
                break;
            case Constants.USER_WORDS:
                userWords(request, response);
                break;
            case Constants.RUN_COMPUTER:
                playComputer(request, response);
                break;
        }
    }

    private void userWords(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        GameEngine currGame = getGame(request);
        String userName = request.getParameter(Constants.USER_NAME);
        Set<Dictionary.Word> playerWords = currGame.getPlayerWords(userName);
        String wordsJson = new Gson().toJson(playerWords);
        response.getWriter().write(wordsJson);
        response.getWriter().flush();
    }

    private void getShowBoard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        GamesManager gamesManager = ServletUtils.getGamesManager(getServletContext());

        String gameTitle = request.getParameter(Constants.GAME_TITLE);
        GameEngine gameBoard = gamesManager.getSpecificGame(gameTitle);
        String boardJson = new Gson().toJson(gameBoard.getBoardAsCells());
        response.getWriter().write(boardJson);
        response.getWriter().flush();
    }


    private void checkWord(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        Gson gson = new Gson();
        GameEngine currGame = getGame(request);

        String responseCanPlay = canPlayerPlay(request,currGame);

        String responseJson;
        if(responseCanPlay.equals("true")){
            String word = request.getParameter(Constants.WORD);
            List<int[]> tilesList = jsonToList(request);
            int tryNumber = new Integer(request.getParameter(Constants.TRY_NUMBER));
            responseJson = "{\"canPlay\":true,\"data\":" + gson.toJson(currGame.isWordValidWithCoordinates(word, tryNumber, tilesList)) + "}";
        }
        else{
            responseJson = "{\"canPlay\":false,\"message\":" + gson.toJson(responseCanPlay) + "}";
        }

        response.getWriter().write(responseJson);
        response.getWriter().flush();
    }

    private void playComputer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        GameEngine currGame = getGame(request);

        String responseCanPlay = canPlayerPlay(request,currGame);
        if(responseCanPlay.equals("true")){
            ComputerTask computer = new ComputerTask(currGame.getCurrentPlayer(), currGame);
            computer.run();
        }
    }

    private String canPlayerPlay(HttpServletRequest request, GameEngine currGame) {

        String retPair = "true";

        if (currGame.isStarted()){
            PlayerData userFromSession = SessionUtils.getLoginUser(request);
            if(!userFromSession.getName().equals(currGame.getCurrentPlayer().getName())) {
                retPair = "Not Your Turn";
            }
        }
        else{
            retPair = "Game is not full yet";
        }

        return retPair;
    }

    private GameEngine getGame(HttpServletRequest request) {

        String currGameTile = (String) request.getSession(false).getAttribute(Constants.GAME_TITLE);
        GamesManager gamesManager = ServletUtils.getGamesManager(getServletContext());
        return gamesManager.getSpecificGame(currGameTile);
    }

    private void isGameDone(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        GameEngine currGame = getGame(request);
        boolean isEnded = currGame.isGameEnded();
        String gameInfo = new Gson().toJson(currGame.getStatistics());
        String responseJson = "{\"isEnded\":" + isEnded + (isEnded ? ",\"data\":" + gameInfo : "") + "}";

        response.getWriter().write(responseJson);
        response.getWriter().flush();
    }

   private void getBoard(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       response.setContentType("application/json");
       GameEngine currGame = getGame(request);
       String boardJson = new Gson().toJson(currGame.getBoard());
       response.getWriter().write(boardJson);
       response.getWriter().flush();
   }

   private void isGameStarted(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       response.setContentType("application/json");
       String isGameStarted = "false";
       GameEngine currGame = getGame(request);

       if(currGame.isFull()){
           isGameStarted = "true";
           currGame.startGame();
       }

       response.getWriter().write(isGameStarted);
       response.getWriter().flush();
   }

   private void exitGame(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {

       response.setContentType("text/html");
       GameEngine currGame = getGame(request);
       PlayerData userFromSession = SessionUtils.getLoginUser(request);

       currGame.retirePlayer(userFromSession.getName());
       request.getSession(true).removeAttribute(Constants.GAME_TITLE);
       userFromSession.setIsPlaying(false);
   }

    private void throwDice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        GameEngine currGame = getGame(request);

        String responseCanPlay = canPlayerPlay(request, currGame);
        String responseJson;
        if (responseCanPlay.equals("true")) {
            responseJson = "{\"canPlay\":true," + "\"data\":" + currGame.getDiceValue() + "}";
        }
        else {
            responseJson = "{\"canPlay\":false," + "\"message\":" + new Gson().toJson(responseCanPlay) + "}";
        }

        response.getWriter().write(responseJson);
        response.getWriter().flush();
    }

    private void updateBoard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        boolean res[] = {false, false};
        GameEngine currGame = getGame(request);
        List<int[]> tilesList = jsonToList(request);

        Gson gson = new Gson();

        String responseCanPlay = canPlayerPlay(request, currGame);
        String responseJson;
        if (responseCanPlay.equals("true")) {
            try {
                if (currGame.updateBoard(tilesList)) {
                    res[0] = true;
                }
            }
            catch (OutOfBoardBoundariesException e) {
                res[1] = true;
            }
            responseJson = "{\"canPlay\":true," + "\"data\":" + gson.toJson(res) + "}";
        }
        else {
            responseJson = gson.toJson("{\"canPlay\":false," + "\"message\":" + gson.toJson(responseCanPlay) + "}");
        }

        response.getWriter().write(responseJson);
        response.getWriter().flush();
    }

    private void gameStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        String nameJson = new Gson().toJson(((PlayerData) request.getSession(false).getAttribute(Constants.LOGIN_USER)).getName());

        String usersJson, gameDetailsJson;
        GameEngine currGame = getGame(request);
        Statistics currGameStatistics = currGame.getStatistics();
        List<Player> gamePlayers = currGame.getPlayers();

        Gson gson = new Gson();

        usersJson = gson.toJson(gamePlayers);
        gameDetailsJson = gson.toJson(currGameStatistics);
        String board = gson.toJson(currGame.getBoardAsCells());

        String bothJson = "[" + usersJson + "," + gameDetailsJson + "," + nameJson + "," + board + "]"; //Put both objects in an array of 3 elements
        response.getWriter().write(bothJson);
        response.getWriter().flush();
    }

    private List<int[]> jsonToList(HttpServletRequest request) {
        String jsonTilesList = request.getParameter(Constants.TILES_LIST);
        return new Gson().fromJson(jsonTilesList, new TypeToken<ArrayList<int[]>>(){}.getType());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}



