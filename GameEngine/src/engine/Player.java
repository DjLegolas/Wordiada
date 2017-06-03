package engine;

import java.util.*;



public class Player {
    public enum Type {
        HUMAN, COMPUTER;
    }
    private short id;

    private String name;
    private float score;
    private Type type;
    private List<String> words;
    static final short MAX_PLAYERS = 6;
    static final short MIN_PLAYERS = 2;

    Player(String name, short id, String type) {
        this.name = name;
        this.id = id;
        this.score = 0;
        if(type == "Human")
            this.type = Type.HUMAN;
        else
            this.type = Type.COMPUTER;
        words = new ArrayList<>();
    }

    void updateScore(String word, float score) {
        this.score += score;
        words.add(word);
    }

    String getName() {
        return name;
    }

    public float getScore() {
        return score;
    }

    List<String> getWords() {
        List<String> l = new ArrayList<>();
        l.addAll(words);
        return l;
    }

    @Override
    public String toString() {
        return String.format("%s id: %d (%s) Score:", name, id, type.name().toLowerCase(), score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id == player.id;

    }


    @Override
    public int hashCode() {
        return id;
    }

}
