package engine.exceptions;

import java.io.FileNotFoundException;

public class DictionaryNotFoundException extends FileNotFoundException {

    public DictionaryNotFoundException () {
        super();
    }

    public DictionaryNotFoundException (String message) {
        super (message);
    }
}
