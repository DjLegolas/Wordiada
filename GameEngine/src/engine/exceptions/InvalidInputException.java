package engine.exceptions;

/**
 * Created by נוי on 24/04/2017.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException  () {}
    public InvalidInputException (String message) {
        super (message);
    }
    public InvalidInputException (Throwable cause) {
        super (cause);
    }
    public InvalidInputException (String message, Throwable cause) {
        super (message, cause);
    }

}
