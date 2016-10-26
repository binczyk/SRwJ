package daniel.biniek.Request;

import daniel.biniek.Controller.ReadProduct;

import javax.xml.soap.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SOAPClient {

    private static ReadProduct readProduct = new ReadProduct();

    public static List<String> get() throws SOAPException {
        return sendRequest("get");
    }

    public static void create(String type, String product) throws SOAPException {
        sendRequest("create", type, readProduct.readName(product), readProduct.readBack(product), readProduct.readPrice(product), readProduct.readAmount(product));
    }

    private static List<String> sendRequest(String method, String... additionalAttribut) {
        List<String> respone = new ArrayList<>();
        SOAPMessage soapResponse;
        try {
            SOAPConnection soapConnection = createConnection();

            // Send SOAP Message to SOAP Server
            String url = "http://localhost:8080/test";
            if (method.equals("get")) {
                soapResponse = soapConnection.call(getProducts(), url);
            } else {
                soapResponse = soapConnection.call(createOrder(additionalAttribut[0], additionalAttribut[1], additionalAttribut[2], additionalAttribut[3], additionalAttribut[4]), url);
            }

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


    private static SOAPMessage getProducts() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://Product.biniek.daniel/ProductService/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("prod", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.addChildElement("getProductsRequest", "prod");
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "getProductsRequest");

        soapMessage.saveChanges();

        return soapMessage;
    }

    private static SOAPMessage createOrder(String... additionalAttribut) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://Product.biniek.daniel/ProductService/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("prod", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("createOrderRequest", "prod");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("arg0");
        soapBodyElem1.addTextNode(additionalAttribut[0]);
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("arg1");
        soapBodyElem2.addTextNode(additionalAttribut[1]);
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("arg2");
        soapBodyElem3.addTextNode(additionalAttribut[2]);
        SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("arg3");
        soapBodyElem4.addTextNode(additionalAttribut[3]);
        SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("arg4");
        soapBodyElem5.addTextNode(additionalAttribut[4]);
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "createOrderRequest");

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

