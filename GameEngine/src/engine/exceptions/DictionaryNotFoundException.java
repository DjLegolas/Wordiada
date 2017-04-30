package engine.exceptions;

import java.io.FileNotFoundException;

public class DictionaryNotFoundException extends FileNotFoundException {
    String fileName;

    public DictionaryNotFoundException (String fileName) {
        super ();
        fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
