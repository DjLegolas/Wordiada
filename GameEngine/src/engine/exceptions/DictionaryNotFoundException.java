package engine.exceptions;

import java.io.FileNotFoundException;

public class DictionaryNotFoundException extends FileNotFoundException {
    private String fileName;

    public DictionaryNotFoundException (String fileName) {
        super("There is no dictionary file name \"" + fileName + "\"");
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
