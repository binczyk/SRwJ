
package mypackage;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for orderType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="orderType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="BUY"/>
 *     &lt;enumeration value="SELL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "orderType", namespace = "http://Product.biniek.daniel/")
@XmlEnum
public enum OrderType {

    BUY,
    SELL;

    public String value() {
        return name();
    }

    public static OrderType fromValue(String v) {
        return valueOf(v);
    }

}
