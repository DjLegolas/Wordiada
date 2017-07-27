package engine.exceptions;

public class NoTitleException extends Exception {
    public NoTitleException() {
        super("This game don't have a title");
    }
}
