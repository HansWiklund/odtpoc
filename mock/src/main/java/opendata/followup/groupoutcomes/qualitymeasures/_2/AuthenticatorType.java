//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.24 at 03:50:40 PM CEST 
//


package opendata.followup.groupoutcomes.qualitymeasures._2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuthenticatorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AuthenticatorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="authenticatorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="authenticatorRoleCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="authenticatorOrganization" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthenticatorType", propOrder = {
    "authenticatorName",
    "authenticatorRoleCode",
    "authenticatorOrganization"
})
public class AuthenticatorType {

    protected String authenticatorName;
    protected String authenticatorRoleCode;
    protected String authenticatorOrganization;

    /**
     * Gets the value of the authenticatorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticatorName() {
        return authenticatorName;
    }

    /**
     * Sets the value of the authenticatorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticatorName(String value) {
        this.authenticatorName = value;
    }

    /**
     * Gets the value of the authenticatorRoleCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticatorRoleCode() {
        return authenticatorRoleCode;
    }

    /**
     * Sets the value of the authenticatorRoleCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticatorRoleCode(String value) {
        this.authenticatorRoleCode = value;
    }

    /**
     * Gets the value of the authenticatorOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticatorOrganization() {
        return authenticatorOrganization;
    }

    /**
     * Sets the value of the authenticatorOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticatorOrganization(String value) {
        this.authenticatorOrganization = value;
    }

}
