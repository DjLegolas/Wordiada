package engine.exceptions;

public class DuplicateLetterException extends Exception {
    String letter;

    public DuplicateLetterException(String letter) {
        super ();
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }
}
