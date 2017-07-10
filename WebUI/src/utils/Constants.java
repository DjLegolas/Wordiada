package utils;

public class Constants  {
    // servlet / session information
    public static final String LOGIN_USER = "loginuser";
    public static final String USER_NAME = "username";
    public static final String GAME_TITLE = "gameTitle";
    public static final String USER_TYPE = "usertype";
    public static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    public static final String GAMES_MANAGER_ATTRIBUTE_NAME = "gamesManager";

    // user type
    public static final String HUMAN = "Human";
    public static final String MACHINE = "Machine";

////////////////////////////////////////////

    public static final String ACTION_TYPE = "ActionType";

    // login servlet
    public static final String PERFOME_LOGIN = "PerformLogIn";
    public static final String CHECK_USER_LOGIN = "CheckUserLogIn";

    // lobby servlet
    public static final String GAME_AND_USER_LIST = "GameAndUserList";
    public static final String LOGOUT = "Logout";
    public static final String JOIN_GAME = "JoinGame";
    public static final String CHECK_USER_PLAYING = "CheckUserPlaying";
    public static final String XML_FILE = "xmlFileName";
    public static final String JOIN_AS_VISITOR="joinGameVisitor";

    // game room servlet
    public static final String DO_MOVE = "doMove";
    public static final String GAME_STATUS = "GameStatus";
    public static final String EXIT_GAME = "ExitGame";

    //public static final String INITIALIZE = "Initialize";
    public static final String IS_GAME_STARTED = "isGameStarted";
    public static final String GET_BOARD = "getBoard";
    public static final String GET_SHOW_BOARD="getShowBoard";
    public static final String IS_GAME_DONE = "isGameDone";
}