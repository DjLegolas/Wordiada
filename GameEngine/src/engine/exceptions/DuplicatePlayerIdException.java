package engine.exceptions;

public class DuplicatePlayerIdException extends Exception {
    private short duplicateId;

    public DuplicatePlayerIdException(short duplicateId) {
        super("Player ID " + duplicateId + "already exists");
        this.duplicateId = duplicateId;
    }

    public short getDuplicateId() {
        return duplicateId;
    }
}
