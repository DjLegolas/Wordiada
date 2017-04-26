package engine.exceptions;

/**
 * Created by נוי on 24/04/2017.
 */
public class WrongPathException extends Exception {

    public WrongPathException () {}
    public WrongPathException (String message) {
        super (message);
    }
    public WrongPathException (Throwable cause) {
        super (cause);
    }
    public WrongPathException (String message, Throwable cause) {
        super (message, cause);
    }
}
