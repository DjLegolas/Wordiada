package Servelet;

import UILogic.GamesManager;
import UILogic.UserManager;
import com.google.gson.Gson;
import logic.Game;
import shared.GameInfo;
import SharedStructures.PlayerData;
import Utils.Constants;
import Utils.ServletUtils;
import Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

@WebServlet(name = "LobbyServlet", urlPatterns = {"/lobby"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)

public class LobbyServlet extends HttpServlet {

    private void checkUserPlaying(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException    {
        response.setContentType("application/json");
        String isAlreadyPlaying = "false";
        PlayerData userFromSession = SessionUtils.getLoginUser(request);

        if(userFromSession.getIsPlaying()){
            isAlreadyPlaying = "true";
        }
        response.getWriter().write(isAlreadyPlaying);
        response.getWriter().flush();
    }

    private void gameAndUserLists(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException    {
        GameInfo gameInfo;

        response.setContentType("application/json");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        GamesManager gamesManager = ServletUtils.getGamesManager(getServletContext());
        Gson gson = new Gson();

        HashMap<String, GameInfo> gamesInfo = gamesManager.getGamesInfosMap();
        HashMap<String, Game> games = gamesManager.getGamesMap();

        Set<String> titles = gamesInfo.keySet();

        for(String title: titles) {
            gameInfo = gamesInfo.get(title);
            if(gameInfo.getGameEmptyOfPlayers()){
                gamesInfo.remove(title);
                games.remove(title);
            }
        }


        String usersJson = gson.toJson(userManager.getUsers());
        String gameDetalisJson = gson.toJson(gamesManager.getGamesInfosMap());

        String bothJson = "["+usersJson+","+gameDetalisJson+"]"; //Put both objects in an array of 2 elements
        response.getWriter().write(bothJson);
        response.getWriter().flush();
    }

    private void joinGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException   {

        response.setContentType("application/json");
        String success = "true";

        PlayerData userFromSession = SessionUtils.getLoginUser(request);
        GamesManager gamesManager = ServletUtils.getGamesManager(getServletContext());

        String gameTitle = request.getParameter(Constants.GAME_TITLE);
        Game gameToJoin = gamesManager.getSpecificGame(gameTitle);
        if(gameToJoin != null)
        {
            String playerName = userFromSession.getName();
            if(!gameToJoin.isActiveGame() && !gameToJoin.isPlayerInGame(playerName) && !gameToJoin.isFull())
            {
                gameToJoin.addPlayer(playerName, userFromSession.getType().name().equals(Constants.HUMAN));
                request.getSession(true).setAttribute(Constants.GAME_TITLE, gameTitle);
                userFromSession.setIsPlaying(true);
            }
            else {
                success = "false";
            }
            response.getWriter().write(success);
            response.getWriter().flush();
        }
    }

    private void logOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException   {

        response.setContentType("text/html");
        PlayerData userFromSession = SessionUtils.getLoginUser(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (userFromSession != null) {
            userManager.removeUser(userFromSession.getName());
            SessionUtils.clearSession(request);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException    {

        String action = request.getParameter(Constants.ACTION_TYPE);
        switch (action)
        {
            case Constants.GAME_AND_USER_LIST:
                gameAndUserLists(request, response);
                break;
            case Constants.JOIN_GAME:
                joinGame(request, response);
                break;
            case Constants.JOIN_AS_VISITOR:
                //joinGameVisitor(request,response);
                break;
            case Constants.LOGOUT:
                logOut(request, response);
                break;
            case Constants.CHECK_USER_PLAYING:
                //checkUserPlaying(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException    {

        response.setContentType("text/html");
        response.setContentType("application/json");

        Part myPart = request.getPart(Constants.XML_FILE);
        InputStream xmlFile = myPart.getInputStream();
        myPart = request.getPart(Constants.DICT_FILE);
        InputStream dictFile = myPart.getInputStream();
        GamesManager gamesManager = ServletUtils.getGamesManager(getServletContext());
        PlayerData userFromSession = SessionUtils.getLoginUser(request);
        String result = gamesManager.addNewGame(xmlFile, dictFile, userFromSession.getName());

        Gson gson = new Gson();

        response.getWriter().write(gson.toJson(result));
        response.getWriter().flush();
    }
}
