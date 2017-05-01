package engine;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

    private List<PlayerData> players ;
    private int numOfTurns;
    private int leftBoxTiles;
    private long playTime;
    private GameDataFromXml gameData;
    private Dictionary dict;

    //TODO: add letters left in box of cards

    public class PlayerData {
        private Player player;

        private PlayerData(Player p) {
            player = p;
        }

        public String getName() {
            return player.getName();
        }

        public List<String> getWords() {

            return player.getWords();
        }

        public float getScore() {
            return player.getScore();
        }
    }

    Statistics(List <Player> inputPlayer, long playTime, int turnsPlayed, GameDataFromXml gd){
        players = new ArrayList<>();
        for (Player player: inputPlayer) {
            players.add(new PlayerData(player));
        }
        this.playTime = playTime / 1000;
        numOfTurns = turnsPlayed;
        leftBoxTiles = gd.getKupaAmount();
        gameData = gd;
        dict = gd.getDictionary();
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
        return dict.hasWord(word) ? dict.getWordAmount(word) : 0;
    }
}