//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.07 at 01:42:20 PM CST 
//


package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;


/**
 * Allow to configure timeout for the wait node
 * 
 * <p>Java class for timeout complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="timeout">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="delay" use="required" type="{http://www.w3.org/2001/XMLSchema}duration" />
 *       &lt;attribute name="then" use="required" type="{http://www.hybris.de/xsd/processdefinition}name" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timeout")
public class Timeout {

    @XmlAttribute(name = "delay", required = true)
    protected Duration delay;
    @XmlAttribute(name = "then", required = true)
    protected String then;

    /**
     * Gets the value of the delay property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getDelay() {
        return delay;
    }

    /**
     * Sets the value of the delay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setDelay(Duration value) {
        this.delay = value;
    }

    /**
     * Gets the value of the then property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThen() {
        return then;
    }

    /**
     * Sets the value of the then property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThen(String value) {
        this.then = value;
    }

}
