package daniel.biniek.Request;

import javax.xml.soap.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SOAPClient {

    public static List<String> get() throws SOAPException {
        return sendRequest("get", new String());
    }

    public static void create(String type, String product) throws SOAPException {
        sendRequest("create", type, product);
    }

    private static List<String> sendRequest(String method, String value, String... additionalAttribut) {
        List<String> respone = new ArrayList<>();

        try {
            SOAPConnection soapConnection = createConnection();

            // Send SOAP Message to SOAP Server
            String url = "http://localhost:8080/test";
            String pr = additionalAttribut.length > 0 ? additionalAttribut[0] : null;
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(method, value, pr), url);

            // Process the SOAP Response
            respone.addAll(getSOAPResponse(soapResponse));

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


    private static SOAPMessage createSOAPRequest(String method, String value, String... additionalAttribut) throws Exception {
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
            soapBodyElem2.addTextNode(additionalAttribut[0]);
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("arg2");
            soapBodyElem3.addTextNode(additionalAttribut[1]);
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI + "createOrderRequest");
        }

        soapMessage.saveChanges();

        return soapMessage;
    }

    private static List<String> getSOAPResponse(SOAPMessage soapResponse) throws Exception {
        List<String> products = new ArrayList<>();
        Iterator iterator = soapResponse.getSOAPBody().getChildElements();
        while (iterator.hasNext()) {
            SOAPElement update = (SOAPElement) iterator.next();
            Iterator ret = update.getChildElements();
            while (ret.hasNext()) {
                SOAPElement e = (SOAPElement) ret.next();
                Iterator item = e.getChildElements();
                while (item.hasNext()) {
                    SOAPElement itemVal = (SOAPElement) item.next();
                    Iterator values = itemVal.getChildElements();
                    while (values.hasNext()) {
                        Text lastElement = (Text) values.next();
                        products.add(lastElement.getValue());
                    }
                }
            }
        }
        return products;
    }
}

