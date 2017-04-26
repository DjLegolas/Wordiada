package engine.exceptions;

public class BoardSizeException extends Exception {
    private short size;
    private short minSize;
    private short maxSize;
    public BoardSizeException() {}
    public BoardSizeException(short size, short minSize, short maxSize) {
        this.size = size;
        this.maxSize = maxSize;
        this.minSize = minSize;
    }

    public short getSize() {
        return size;
    }

    public short getMinSize() {
        return minSize;
    }

    public short getMaxSize() {
        return maxSize;
    }
}
