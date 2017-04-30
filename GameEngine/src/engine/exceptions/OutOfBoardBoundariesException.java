package engine.exceptions;


public class OutOfBoardBoundariesException extends Exception {

    public OutOfBoardBoundariesException() {}
    public OutOfBoardBoundariesException(String message) {
        super (message);
    }
    public OutOfBoardBoundariesException(Throwable cause) {
        super (cause);
    }
    public OutOfBoardBoundariesException(String message, Throwable cause) {
        super (message, cause);
    }
}
