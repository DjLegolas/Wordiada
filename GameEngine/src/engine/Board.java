package engine;

import engine.exceptions.BoardSizeException;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private short size;
    private Map<String, String> board = new HashMap<>();

    public Board(short size) throws BoardSizeException{
        if (size < 5 || size > 50) {
            throw new BoardSizeException(size, (short)5, (short)50);
        }
    }

    public Map<String, String> getBoard() {
        return new HashMap<>(board);
    }


}
