package engine.exceptions;
import java.lang.String;
import java.lang.Throwable;

/**
 * Created by נוי on 24/04/2017.
 */
public class NotXmlFileException extends Exception {

        public NotXmlFileException () {}
        public NotXmlFileException (String message) {
            super (message);
        }
        public NotXmlFileException (Throwable cause) {
            super (cause);
        }
        public NotXmlFileException (String message, Throwable cause) {
            super (message, cause);
        }
}

