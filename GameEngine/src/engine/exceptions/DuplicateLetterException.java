package engine.exceptions;

/**
 * Created by נוי on 24/04/2017.
 */
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
