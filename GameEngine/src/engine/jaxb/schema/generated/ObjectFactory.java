package engine.jaxb.schema.generated;

import java.util.List;
import javax.xml.bind.JAXBElement;

import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the engine.xml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and GameManager
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Type_QNAME = new QName("", "Type");
    private final static QName _Score_QNAME = new QName("", "Score");
    private final static QName _BoardSize_QNAME = new QName("", "BoardSize");
    private final static QName _Frequency_QNAME = new QName("", "Frequency");
    private final static QName _Sign_QNAME = new QName("", "Sign");
    private final static QName _Name_QNAME = new QName("", "Name");
    private final static QName _DictionaryFileName_QNAME = new QName("", "DictionaryFileName");



    //engine.jaxb.schema.generated.ObjectFactory objectFactory = new   engine.jaxb.schema.generated.ObjectFactory();
  //  JAXBContext jaxbContext tring());
    /**
     *
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: engine.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Player }
     * 
     */
    public Player createPlayer() {
        return new Player();
    }

    /**
     * Create an instance of {@link Letters }
     * 
     */
    public Letters createLetters() {
        return new Letters();
    }

    /**
     * Create an instance of {@link Letter }
     * 
     */
    public Letter createLetter() {
        return new Letter();
    }

    /**
     * Create an instance of {@link GameType }
     * 
     */
    public GameType createGameType() {
        return new GameType();
    }

    /**
     * Create an instance of {@link Structure }
     * 
     */
    public Structure createStructure() {
        return new Structure();
    }

    /**
     * Create an instance of {@link GameDescriptor }
     * 
     */
    public GameDescriptor createGameDescriptor() {
        return new GameDescriptor();
    }

    /**
     * Create an instance of {@link Players }
     * 
     */
    public Players createPlayers() {
        return new Players();
    }

    /**
     * Create an instance of {@link DynamicPlayers }
     * 
     */
    public DynamicPlayers createDynamicPlayers() {
        return new DynamicPlayers();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Type")
    public JAXBElement<String> createType(String value) {
        return new JAXBElement<String>(_Type_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Score")
    public JAXBElement<Byte> createScore(Byte value) {
        return new JAXBElement<Byte>(_Score_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "BoardSize")
    public JAXBElement<Byte> createBoardSize(Byte value) {
        return new JAXBElement<Byte>(_BoardSize_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Frequency")
    public JAXBElement<Double> createFrequency(Double value) {
        return new JAXBElement<Double>(_Frequency_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link String }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Sign")
    public JAXBElement<List<String>> createSign(List<String> value) {
        return new JAXBElement<List<String>>(_Sign_QNAME, ((Class) List.class), null, ((List<String> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link String }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Name")
    public JAXBElement<List<String>> createName(List<String> value) {
        return new JAXBElement<List<String>>(_Name_QNAME, ((Class) List.class), null, ((List<String> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DictionaryFileName")
    public JAXBElement<String> createDictionaryFileName(String value) {
        return new JAXBElement<String>(_DictionaryFileName_QNAME, String.class, null, value);
    }

}
