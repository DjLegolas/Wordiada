package engine.exceptions;


public class NotEnoughLettersException extends Exception {
    private int expectedAmount;
    private int currentAmount;

    public NotEnoughLettersException(int expected, int current) {
        this.currentAmount = current;
        this.expectedAmount = expected;
    }

    public int getExpectedAmount() {
        return expectedAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }
}
