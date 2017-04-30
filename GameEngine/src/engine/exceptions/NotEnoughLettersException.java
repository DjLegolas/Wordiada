package engine.exceptions;


public class NotEnoughLettersException extends Exception {
    private int expectedAmount;
    private int currentAmount;

    public NotEnoughLettersException() {}
    public NotEnoughLettersException(int expected, int current){
        this.currentAmount = current;
        this.expectedAmount = expected;
    }

    public NotEnoughLettersException(String message) {
        super (message);
    }
    public NotEnoughLettersException(Throwable cause) {
        super (cause);
    }
    public NotEnoughLettersException(String message, Throwable cause) {
        super(message, cause);
    }
}
