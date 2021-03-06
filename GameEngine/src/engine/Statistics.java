package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Statistics {

    private List<PlayerData> players ;
    private List<Letter> letters;
    private int numOfTurns;
    private int leftBoxTiles;
    private long playTime;
    private Dictionary dict;

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

    Statistics(List <Player> inputPlayer, long playTime, int turnsPlayed, GameDataFromXml gd){
        players = new ArrayList<>();
        for (Player player: inputPlayer) {
            players.add(new PlayerData(player));
        }
        letters = new ArrayList<>();
        for (GameDataFromXml.DataLetter l: gd.getKupa()) {
            letters.add(new Letter(l));
        }
        this.playTime = playTime / 1000;
        numOfTurns = turnsPlayed;
        leftBoxTiles = gd.getKupaAmount();
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
        Dictionary.Word dictWord = dict.hasWord(word);
        return dictWord != null ? dict.getWordAmount(word) : 0;
    }

    public List<Letter> getLetters() {
        return letters;
    }
}