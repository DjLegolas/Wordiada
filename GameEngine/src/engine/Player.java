package engine;

import java.util.*;

public class Player {

    private String name;
    private float score;
    private List<String> words;
    static final short MAX_PLAYERS = 6;
    static final short MIN_PLAYERS = 2;

    public Player(String name) {
        this.name = name;
        score = 0;
        words = new ArrayList<>();
    }

    public void updateScore(String word, float score) {
        this.score += score;
        words.add(word);
    }

    public String getName() {
        return name;
    }

    public float getScore() {
        return score;
    }

    public List<String> getWords() {
        List<String> l = new ArrayList<>();
        l.addAll(words);
        return l;
    }
}
