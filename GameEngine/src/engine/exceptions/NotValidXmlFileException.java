package engine.exceptions;

public class NotValidXmlFileException extends Exception{
    public NotValidXmlFileException() {
        super("Not valid Xml file!");
    }

    public NotValidXmlFileException(String message) {
        super(message);
    }
}
