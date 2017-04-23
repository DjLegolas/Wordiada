
package engine.jabx.schema.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
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
 *       &lt;all>
 *         &lt;element ref="{}Sign"/>
 *         &lt;element ref="{}Score"/>
 *         &lt;element ref="{}Frequency"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Letter")
public class Letter {

    @XmlList
    @XmlElement(name = "Sign", required = true)
    protected List<String> sign;
    @XmlElement(name = "Score")
    protected byte score;
    @XmlElement(name = "Frequency")
    protected double frequency;

    /**
     * Gets the value of the sign property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sign property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSign().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSign() {
        if (sign == null) {
            sign = new ArrayList<String>();
        }
        return this.sign;
    }

    /**
     * Gets the value of the score property.
     * 
     */
    public byte getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     */
    public void setScore(byte value) {
        this.score = value;
    }

    /**
     * Gets the value of the frequency property.
     * 
     */
    public double getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     * 
     */
    public void setFrequency(double value) {
        this.frequency = value;
    }

}
