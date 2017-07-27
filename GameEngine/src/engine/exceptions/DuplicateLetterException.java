package engine.exceptions;

public class DuplicateLetterException extends Exception {
    private String letter;

    public DuplicateLetterException(String letter) {
        super("The letter \'" + letter + "\' has a duplicate entry!");
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }
}
