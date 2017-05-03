package engine.exceptions;

public class WinTypeException extends Exception {
    private String winType;

    public WinTypeException(String winType) {
        super();
        this.winType = winType;
    }

    public String getWinType() {
        return winType;
    }
}
