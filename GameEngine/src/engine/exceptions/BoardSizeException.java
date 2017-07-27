package engine.exceptions;

public class BoardSizeException extends Exception {
    private short size;
    private short minSize;
    private short maxSize;

    public BoardSizeException(short size, short minSize, short maxSize) {
        super("Invalid board size!\n" +
                "Min size: " + minSize + "\n" +
                "Max size: " + maxSize+ "\n" +
                "Actual size: " + size);
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
