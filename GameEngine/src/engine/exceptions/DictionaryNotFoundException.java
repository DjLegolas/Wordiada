package engine.exceptions;

import java.io.FileNotFoundException;

public class DictionaryNotFoundException extends FileNotFoundException {
    private String fileName;

    public DictionaryNotFoundException (String fileName) {
        super ();
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
