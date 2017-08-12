package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Statistics {

    private List<PlayerData> players ;
    private List<Letter> letters;
    private PlayerData currentPlayer;
    private String winnerName;
    private int numOfTurns;
    private int leftBoxTiles;
    private long playTime;
    private Dictionary dict;
    private String gameTitle;
    private int totalPlayers;
    private int boardSize;
    private char[][] board;
    private boolean isGameActive;
    private String organizer;
    private String dictName;
    private int lettersAmount;

    public class PlayerData {
        private Player player;

        private PlayerData(Player p) {
            player = p;
        }

        public String getName() {
            return player.getName();
        }

        public List<String> getWords() {
            List<String> list = new ArrayList<>();
            for (Map.Entry<Dictionary.Word, Integer> entry: player.getWords().entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    list.add(entry.getKey().getWord());
                }
            }
            return list;
        }

        public float getScore() {
            return player.getScore();
        }
    }

    public class Letter {
        private GameDataFromXml.DataLetter letter;

        private Letter(GameDataFromXml.DataLetter l) {
            letter = l;
        }

        public String getLetter() {
            return letter.getLetter().getSign().get(0);
        }

        public int getAmount() {
            return letter.getAmount();
        }
    }

    Statistics(boolean isStarted, GameDataFromXml gd, String organizer, Player currentPlayer, List<Player> inputPlayer, long playTime, int turnsPlayed, Player winner){
        players = new ArrayList<>();
        this.currentPlayer = null;
        for (Player player: inputPlayer) {
            PlayerData playerData = new PlayerData(player);
            players.add(playerData);
            if (player == currentPlayer) {
                this.currentPlayer = playerData;
            }
            if (player == winner) {
                this.winnerName = playerData.player.getName();
            }
        }
        letters = new ArrayList<>();
        for (GameDataFromXml.DataLetter letter: gd.getKupa()) {
            letters.add(new Letter(letter));
        }
        this.lettersAmount = letters.size();
        this.playTime = playTime / 1000;
        numOfTurns = turnsPlayed;
        leftBoxTiles = gd.getKupaAmount();
        dict = gd.getDictionary();
        this.dictName = gd.getDictFileName();
        gameTitle = gd.getGameTitle();
        totalPlayers = gd.getTotalPlayers();
        boardSize = gd.getBoard().getBoardSize();
        board = gd.getBoard().getBoard_onlySigns();
        this.isGameActive = isStarted;
        this.organizer = organizer;
    }

    public long getTime() {
        return playTime;
    }

    public int getNumOfTurns() {
        return numOfTurns;
    }

    public int getLeftBoxTiles() {
        return leftBoxTiles;
    }

    public List<PlayerData> getPlayers() {
        return players;
    }

    public long getTotalWords() {
        return dict.getNumberOfWords();
    }

    public long getWordCount(String word) {
        Dictionary.Word dictWord = dict.hasWord(word);
        return dictWord != null ? dict.getWordAmount(word) : 0;
    }

    public List<Letter> getLetters() {
        return letters;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public char[][] getBoard() {
        return board;
    }

    public PlayerData getCurrentPlayer() {
        return currentPlayer;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getDictName() {
        return dictName;
    }

    public int getLettersAmount() {
        return lettersAmount;
    }
}