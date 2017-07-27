package engine.exceptions;

import java.util.List;

public class OutOfBoardBoundariesException extends Exception {
    private List<Integer[]> points;

    public OutOfBoardBoundariesException() {
        super("At least one of the tiles is out of border bounds");
    }

    // TODO: switch to this c'tor
    public OutOfBoardBoundariesException(List<Integer[]> points) {
        super("You selected " + points.size() + " points out of vorder bounds");
        this.points = points;
    }

    public List<Integer[]> getPoints() {
        return points;
    }
}
