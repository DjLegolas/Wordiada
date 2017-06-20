package desktopUI.scoreDetail;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class WordDetails {
    private final SimpleStringProperty word = new SimpleStringProperty();
    private final SimpleLongProperty amount = new SimpleLongProperty();
    private final SimpleIntegerProperty score = new SimpleIntegerProperty();

    public WordDetails(String word, long amount, int score) {
        this.word.set(word);
        this.amount.set(amount);
        this.score.set(score);
    }

    public String getWord() {
        return word.get();
    }

    public long getAmount() {
        return amount.get();
    }

    public int getScore() {
        return score.get();
    }
}