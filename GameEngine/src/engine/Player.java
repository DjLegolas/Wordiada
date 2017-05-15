package engine;

import java.util.*;

class Player {

    private String name;
    private float score;
    private List<String> words;
    static final short MAX_PLAYERS = 6;
    static final short MIN_PLAYERS = 2;

    Player(String name) {
        this.name = name;
        score = 0;
        words = new ArrayList<>();
    }

    void updateScore(String word, float score) {
        this.score += score;
        words.add(word);
    }

    String getName() {
        return name;
    }

    float getScore() {
        return score;
    }

    List<String> getWords() {
        List<String> l = new ArrayList<>();
        l.addAll(words);
        return l;
    }
}
