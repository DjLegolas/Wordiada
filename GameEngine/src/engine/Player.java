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
    private Map<Dictionary.Word, Integer> words;
    static final short MAX_PLAYERS = 6;
    static final short MIN_PLAYERS = 2;

    Player(String name, short id, String type) {
        this.name = name;
        this.id = id;
        this.score = 0;
        if(type.equals("Human"))
            this.type = Type.HUMAN;
        else
            this.type = Type.COMPUTER;
        words = new HashMap<>();
    }

    void updateScore(Dictionary.Word word, float score) {
        this.score += score;
        words.put(word, words.containsKey(word) ? words.get(word) + 1 : 1);
    }

    public String getName() {
        return name;
    }

    public float getScore() {
        return score;
    }

    public String getType() {
        return type.name().toLowerCase();
    }

    public short getId() {
        return id;
    }

    Map<Dictionary.Word, Integer> getWords() {
        return new HashMap<>(this.words);
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
