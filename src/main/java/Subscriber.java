import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class Subscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(Subscriber.class);
    private static final String NO_GREETING = "no greeting";

    private String clientID;
    private Connection connection;
    private MessageConsumer messageConsumer;

    public void create(String clientID,String topicName) throws JMSException{
        this.clientID = clientID;

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);

        connection = connectionFactory.createConnection();
        connection.setClientID(clientID);
        connection.start();

        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(topicName);

        messageConsumer = session.createConsumer(topic);

    }


    public  String getGreeting (int timeout) throws  JMSException{
        String greeting = NO_GREETING;

        Message message = messageConsumer.receive(timeout);;
        if ( message != null ){
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            LOGGER.debug(clientID+ ": received message with text='{}'",text);
            greeting = "Hello "+text +"!";
        }else {
            LOGGER.debug(clientID + ": no message received");
        }

        LOGGER.info("greeting={}",greeting);

        return  greeting;
    }

    public void closeConnection() throws  JMSException{
        connection.close();
    }
}