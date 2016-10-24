package daniel.biniek.Request;

import daniel.biniek.product.Product;
import daniel.biniek.product.ProductOb;

import javax.xml.soap.*;
import java.util.*;

public class SOAPClient {

    public static List<ProductOb> get() throws SOAPException {
        return sendRequest("get", new String());
    }

    public static void create(String type, ProductOb product) throws SOAPException {
        sendRequest("create", type, product);
    }

    private static List<ProductOb> sendRequest(String method, String value, ProductOb ... additionalAttribut) {
        List<ProductOb> respone = new ArrayList<>();

        try {
            SOAPConnection soapConnection = createConnection();

            // Send SOAP Message to SOAP Server
            String url = "http://localhost:8080/test";
            ProductOb pr = additionalAttribut.length > 0 ? additionalAttribut[0]:null;
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(method, value, pr), url);

            // Process the SOAP Response
            respone = getSOAPResponse(soapResponse);

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        } finally {
            return respone;
        }
    }


    private static SOAPConnection createConnection() throws SOAPException {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        return soapConnectionFactory.createConnection();
    }


    private static SOAPMessage createSOAPRequest(String method, String value, ProductOb additionalAttribut) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://Product.biniek.daniel/ProductService/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("prod", serverURI);

        // SOAP Body
        if (method.equals("get")) {
            SOAPBody soapBody = envelope.getBody();
            soapBody.addChildElement("getProductsRequest", "prod");
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI + "getProductsRequest");
        } else if (method.equals("create")) {
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("createOrderRequest", "prod");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("arg0");
            soapBodyElem1.addTextNode(value);
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("arg1");
            soapBodyElem2.addTextNode(additionalAttribut.getName());
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("arg2");
            soapBodyElem3.addTextNode(additionalAttribut.getBackName());
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI + "createOrderRequest");
        }

        soapMessage.saveChanges();

        return soapMessage;
    }

    private static List<ProductOb> getSOAPResponse(SOAPMessage soapResponse) throws Exception {
        List<ProductOb> products = new ArrayList<>();
        Iterator iterator = soapResponse.getSOAPBody().getChildElements();
        while (iterator.hasNext()) {
            SOAPElement update = (SOAPElement) iterator.next();
            Iterator ret = update.getChildElements();
            while (ret.hasNext()) {
                SOAPElement e = (SOAPElement) ret.next();
                Iterator item = e.getChildElements();
                while (item.hasNext()) {
                    Map<String, String> productMap = new HashMap<>();
                    SOAPElement itemVal = (SOAPElement) item.next();
                    Iterator values = itemVal.getChildElements();
                    while (values.hasNext()) {
                        SOAPElement lastElement = (SOAPElement) values.next();
                        productMap.put(lastElement.getLocalName(), lastElement.getValue());
                    }
                    products.add(preperProduct(productMap));
                }
            }
        }
        return products;
    }

    private static ProductOb preperProduct(Map<String, String> productMap) {
        return new ProductOb(productMap.get("name"), Long.parseLong(productMap.get("id")),
                Float.parseFloat(productMap.get("priceBuy")), Float.parseFloat(productMap.get("priceSell")), productMap.get("backName"));
    }
}

