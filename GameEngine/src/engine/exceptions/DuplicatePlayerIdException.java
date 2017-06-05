package engine.exceptions;

public class DuplicatePlayerIdException extends Exception {
    private short duplicateId;

    public DuplicatePlayerIdException(short duplicateId) {
        super();
        this.duplicateId = duplicateId;
    }

    public short getDuplicateId() {
        return duplicateId;
    }
}
