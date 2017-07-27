package engine.exceptions;

public class WinTypeException extends Exception {
    private String winType;

    public WinTypeException(String winType) {
        super("Win mod \"" + winType + "\" is not supported!");
        this.winType = winType;
    }

    public String getWinType() {
        return winType;
    }
}
