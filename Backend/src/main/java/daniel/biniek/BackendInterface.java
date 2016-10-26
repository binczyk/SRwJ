package daniel.biniek;

import daniel.biniek.JMS.QueueCode;
import daniel.biniek.product.ProductOb;
import daniel.biniek.product.ProductSender;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.jms.Queue;
import java.net.ServerSocket;
import java.util.*;

public class BackendInterface implements Runnable {

    private static String threadName;
    private static List<ProductOb> sell = new ArrayList<>();
    private static List<ProductOb> buy = new ArrayList<>();
    private static List<ProductOb> all = new ArrayList<>();

    public static void main(String arg[]) throws Exception {
        ServerSocket socket = new ServerSocket(0);
        threadName = String.valueOf(socket.getLocalPort());
        socket.close();

        BackendInterface backend = new BackendInterface(threadName);
        backend.start();
    }

    public static void sendProductToQueue(String backNo) {
        ProductSender productSender = new ProductSender();

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            connectionFactory.setTrustAllPackages(true);
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
            Destination destination = session.createQueue(QueueCode.BACK_TO_MT_QUEUE.toString());
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            ProductOb productOb = productSender.randomProduct();
            productOb.setBackName(QueueCode.MT_TO_BACK_QUEUE.toString() + "." + backNo);
            TextMessage message = session.createTextMessage();
            message.setText(getPrepareProduct(productOb));
            message.setJMSType("Product");
            message.setStringProperty("WDSR", "ProductProcessor");
            producer.send(message);
            producer.close();
            System.out.println(productOb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getPrepareProduct(ProductOb productOb) {
        StringBuilder sb = new StringBuilder();
        sb.append(productOb.getName());
        sb.append(";");
        sb.append(productOb.getBackName());
        return sb.toString();
    }

    public static String receiveMessage(String backNo) throws JMSException {
        Map<String, String> productMap = new HashMap<>();
        ActiveMQConnection connection = null;
        QueueSession session = null;
        QueueBrowser browser = null;
        Queue queue;
        String perf = new String();
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            factory.setTrustAllPackages(true);
            connection = (ActiveMQConnection) factory.createConnection();

            session = connection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
            queue = session.createQueue(QueueCode.MT_TO_BACK_QUEUE.toString() + "." + backNo);
            connection.start();
            browser = session.createBrowser(queue);
            Enumeration<?> messagesInQueue = browser.getEnumeration();

            while (messagesInQueue.hasMoreElements()) {
                Message message = (Message) messagesInQueue.nextElement();
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String[] parts = textMessage.getText().split(";");
                    perf = "Method: " + parts[1] + ", product: " + parts[0];
                    System.out.println("Method: " + parts[1] + ", product: " + parts[0] + ", amount: " + parts[3] + ", price: " + parts[2]);
                    productMap.put("name", parts[0]);
                    productMap.put("price", parts[2]);
                    productMap.put("amount", parts[3]);

                    ProductOb product = generateProduct(productMap);

                    if(parts[1].equals("Sell") && !sell.contains(product) && !all.contains(product)){
                        product.setId(null);
                        sell.add(product);
                    }else if(parts[1].equals("Buy") && !buy.contains(product)){
                        buy.add(product);
                        MessageConsumer messageConsumer = session.createConsumer(queue);
                        messageConsumer.receive();

                    }

                    if(!all.contains(product)){
                        all.add(product);
                    }

                    performTransaction();
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
           /* connection.stop();
            connection.destroyDestination(ActiveMQDestination.transform(queue));*/
            session.commit();
            browser.close();
            connection.close();
           return perf;
        }
    }

    private static void performTransaction() {
        if(!sell.isEmpty() && !buy.isEmpty()){
            for (ProductOb s : sell){
                for (ProductOb b : buy){
                    if (canBuy(s, b)) {
                        buy(s, b);
                    }
                }
            }
        }
    }

    private static void buy(ProductOb s, ProductOb b) {
        System.out.println("Product buy. Name: " + b.getName() + " price: " + s.getPrice() + " amount: " + s.getAmount());
        preapareNewValues(s, b);
    }

    private static void preapareNewValues(ProductOb s, ProductOb b) {
        ProductOb newSellVal = new ProductOb();
        newSellVal.setAmount(s.getAmount() - b.getAmount());
        newSellVal.setName(s.getName());
        newSellVal.setPrice(s.getPrice());

        sell.add(newSellVal);
        buy.remove(b);
        sell.remove(s);
    }

    private static boolean canBuy(ProductOb s, ProductOb b) {
        return s.getName().equals(b.getName())
                && s.getAmount() >= b.getAmount()
                && s.getPrice() <= b.getPrice();
    }

    private static ProductOb generateProduct(Map<String, String> productMap) {
        ProductOb newProduct = new ProductOb();
        newProduct.setName(productMap.get("name"));
        newProduct.setAmount(Long.parseLong(productMap.get("amount")));
        newProduct.setPrice(Double.parseDouble(productMap.get("price")));

        return newProduct;
    }

    @Override
    public void run() {
        sendProductToQueue(threadName);
        try {
            while (true) {
                System.out.println("Refresh\n");
                if(receiveMessage(threadName).isEmpty()){
                    Thread.sleep(3000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Starting " + threadName);
        run();
    }

    public BackendInterface(String name) {
        threadName = name;
        System.out.println("Creating " +  threadName);
    }
}




