package engine.exceptions;


public class OutOfBoardBoandriesException extends Exception {

    public OutOfBoardBoandriesException() {}
    public OutOfBoardBoandriesException(String message) {
        super (message);
    }
    public OutOfBoardBoandriesException(Throwable cause) {
        super (cause);
    }
    public OutOfBoardBoandriesException(String message, Throwable cause) {
        super (message, cause);
    }
}
