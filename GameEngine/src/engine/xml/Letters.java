
package engine.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Letter" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="target-deck-size" use="required" type="{http://www.w3.org/2001/XMLSchema}short" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "letter"
})
@XmlRootElement(name = "Letters")
public class Letters {

    @XmlElement(name = "Letter", required = true)
    protected List<Letter> letter;
    @XmlAttribute(name = "target-deck-size", required = true)
    protected short targetDeckSize;

    /**
     * Gets the value of the letter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the letter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLetter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Letter }
     * 
     * 
     */
    public List<Letter> getLetter() {
        if (letter == null) {
            letter = new ArrayList<Letter>();
        }
        return this.letter;
    }

    /**
     * Gets the value of the targetDeckSize property.
     * 
     */
    public short getTargetDeckSize() {
        return targetDeckSize;
    }

    /**
     * Sets the value of the targetDeckSize property.
     * 
     */
    public void setTargetDeckSize(short value) {
        this.targetDeckSize = value;
    }

}
