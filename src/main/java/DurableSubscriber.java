import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DurableSubscriber {
    private static  final Logger LOGGER = LoggerFactory.getLogger(DurableSubscriber.class);
    private static final String NO_GREETING = "no greeting";

    private String clientID;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    private  String subscriptionName;

    public  void create (String clientID,String topicName,String subscriptionName) throws JMSException {
        this.clientID = clientID;
        this.subscriptionName = subscriptionName;

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        connection = connectionFactory.createConnection();
        connection.setClientID(clientID);

        session  = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicName);

        consumer = session.createDurableSubscriber(topic,subscriptionName);

        connection.start();
    }

    public  void removeDurableSubscriber() throws JMSException {
        consumer.close();
        session.unsubscribe(subscriptionName);
    }

    public  void close() throws JMSException {
        connection.close();
    }

    public String getGreeting(int timeout) throws JMSException {
        String greeting =  NO_GREETING;

        Message message = consumer.receive(timeout);

        if (message != null){
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            LOGGER.debug(clientID+ ": received message with text={}'",text);
            greeting = "Hello " + text+"!";
        }else {
            LOGGER.debug(clientID + ":no messaage received");
        }
        LOGGER.info("greeting={}",greeting);
        return  greeting;
    }
}
