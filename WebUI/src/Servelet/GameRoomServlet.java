
package Servelet;

import UILogic.GamesManager;
import com.google.gson.Gson;
// import javafx_ui.gamePane.GameController;
import engine.GameEngine;
import logic.Game;
import logic.Player;
import shared.GameInfo;
import SharedStructures.PlayerData;
import Utils.Constants;
import Utils.ServletUtils;
import Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "GameRoomServlet", urlPatterns = {"/gamingRoom"})
public class GameRoomServlet extends HttpServlet {
    GameEngine controller;
    private boolean m_GameFull = false;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        //response.setContentType("text/html");
        String action = request.getParameter(Constants.ACTION_TYPE);
        switch (action) {
            case Constants.DO_MOVE:
                handleDoMove(request,response);
                break;
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
        }
    }

    private void getShowBoard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        GamesManager gamesManager = ServletUtils.getGamesManager(getServletContext());

        String gameTitle = request.getParameter(Constants.GAME_TITLE);
        Game gameBoard = gamesManager.getSpecificGame(gameTitle);
        //BoardInfo board = gameBoard.getOriginalBoard();

        //SimpleBoard responseBoard = new SimpleBoard(board.getBoard(), board.getRowBlocks(), board.getColBlocks());
        String boardJson = new Gson().toJson(gameBoard.getBoard());
        response.getWriter().write(boardJson);
        response.getWriter().flush();
    }


        private void handleDoMove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            response.setContentType("application/json");
            Gson gson = new Gson();
            Game currGame = getGame(request);

            //load chosen move to game info
            currGame.getGameInfo().setChosenRow(Integer.parseInt(request.getParameter("row")));
            currGame.getGameInfo().setChosenCol(Integer.parseInt(request.getParameter("column")));

            String responseCanPlay = canPlayerPlay(request,currGame);

            if(responseCanPlay.equals("true")){
                //controller.makeMoveClicked(currGame);
            }
            else{
                currGame.getGameInfo().setErrorFound(true);
                currGame.getGameInfo().setErrorMsg(responseCanPlay);
            }

            String gameInfo = gson.toJson(currGame.getGameInfo());
            String board = gson.toJson(currGame.getBoard());

            String bothJson = "[" + gameInfo + "," + board + "]";

            response.getWriter().write(bothJson);
            response.getWriter().flush();

            currGame.getGameInfo().setErrorFound(false);
            currGame.getGameInfo().setErrorMsg("");
        }


        private String canPlayerPlay(HttpServletRequest request, Game currGame) {

            String retPair = "true";

            if (m_GameFull){
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

    private Game getGame(HttpServletRequest request) {

        String currGameTile = (String) request.getSession(false).getAttribute(Constants.GAME_TITLE);
        GamesManager gamesManager = ServletUtils.getGamesManager(getServletContext());
        return gamesManager.getSpecificGame(currGameTile);
    }

    private GameInfo getGameInfo(HttpServletRequest request) {

        String currGameTile = (String) request.getSession(false).getAttribute(Constants.GAME_TITLE);
        GamesManager gamesManager = ServletUtils.getGamesManager(getServletContext());
        return gamesManager.getSpecificGameInfo(currGameTile);
    }

    private void isGameDone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        Game currGame = getGame(request);
        String gameInfo = new Gson().toJson(currGame.getGameInfo());

        response.getWriter().write(gameInfo);
        response.getWriter().flush();
    }


   private void getBoard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException   {
       response.setContentType("application/json");
       Game currGame = getGame(request);
       // SimpleBoard responseBoard = new SimpleBoard(board.getBoard(), board.getRowBlocks(), board.getColBlocks());
       String boardJson = new Gson().toJson(currGame.getBoard());
       response.getWriter().write(boardJson);
       response.getWriter().flush();
   }

   private void isGameStarted(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException    {
       response.setContentType("application/json");
       String isGameStarted = "false";
       Game currGame = getGame(request);

       if(currGame.isFull()){
           isGameStarted = "true";
           currGame.setIsActiveGame(true);
           controller.setGameStarted(true);
           //controller.startGameClicked(currGame);
       }

       response.getWriter().write(isGameStarted);
       response.getWriter().flush();
   }


   private void exitGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException    {

       response.setContentType("text/html");
       Game currGame = getGame(request);
       PlayerData userFromSession = SessionUtils.getLoginUser(request);

       //controller.quitButtonClicked(currGame, controller.getGameStarted(),userFromSession.getName());
       request.getSession(true).removeAttribute(Constants.GAME_TITLE);
       userFromSession.setIsPlaying(false);
   }


    private void gameStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        String nameJson = new Gson().toJson(((PlayerData) request.getSession(false).getAttribute(Constants.LOGIN_USER)).getName());

        String usersJson, gameDetailsJson;
        Game currGame = getGame(request);
        GameInfo currGameInfo = getGameInfo(request);
        ArrayList<Player> gamePlayers = currGame.getPlayers();

        if(currGame.getGameInfo().getTotalPlayers() == currGame.getPlayers().size()){
            m_GameFull = true;
        }

        Gson gson = new Gson();

        usersJson = gson.toJson(gamePlayers);
        gameDetailsJson = gson.toJson(currGameInfo);
        String board = gson.toJson(currGame.getBoard());

        String bothJson = "[" + usersJson + "," + gameDetailsJson + "," + nameJson + "," + board + "]"; //Put both objects in an array of 3 elements
        response.getWriter().write(bothJson);
        response.getWriter().flush();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}



