package daniel.biniek.Request;

import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.util.Iterator;

public class SOAPClient {

    public static String start() throws SOAPException {
        String respone = "";

        try {
            SOAPConnection soapConnection = createConnection();

            // Send SOAP Message to SOAP Server
            String url = "http://localhost:8080/test";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);

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


    private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://Product.biniek.daniel/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("prod", serverURI);

        /*
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:prod="http://Product.biniek.daniel/">
            <soapenv:Header/>
                <soapenv:Body>
                    <prod:getProducts/>
                </soapenv:Body>
        </soapenv:Envelope>
         */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.addChildElement("getProducts", "prod");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "getProducts");

        soapMessage.saveChanges();

        /* Print the request message */
        /*System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();*/

        return soapMessage;
    }

    private static String getSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();

        Iterator iterator = soapResponse.getSOAPBody().getChildElements();
        while (iterator.hasNext()) {
            SOAPElement update = (SOAPElement) iterator.next();
            String products = update.getAttribute("return");
            System.out.println(products);
            java.util.Iterator i = update.getChildElements();
            while (i.hasNext()) {
                SOAPElement e = (SOAPElement) i.next();
                String name = e.getLocalName();
                String value = e.getValue();
                System.out.println(value);
            }
        }
        System.out.println();
/*
        System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);*/

        return "";
    }
}

