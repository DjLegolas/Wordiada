
package engine.jabx.schema.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="winner-according-to" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="WordCount"/>
 *             &lt;enumeration value="WordScore"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="gold-fish-mode" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})
@XmlRootElement(name = "GameType")
public class GameType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "winner-according-to", required = true)
    protected String winnerAccordingTo;
    @XmlAttribute(name = "gold-fish-mode")
    protected Boolean goldFishMode;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the winnerAccordingTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWinnerAccordingTo() {
        return winnerAccordingTo;
    }

    /**
     * Sets the value of the winnerAccordingTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWinnerAccordingTo(String value) {
        this.winnerAccordingTo = value;
    }

    /**
     * Gets the value of the goldFishMode property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGoldFishMode() {
        return goldFishMode;
    }

    /**
     * Sets the value of the goldFishMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGoldFishMode(Boolean value) {
        this.goldFishMode = value;
    }

}
