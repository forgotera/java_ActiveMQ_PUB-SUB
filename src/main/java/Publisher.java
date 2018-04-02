import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publisher {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Publisher.class);
    private String clientID;
    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    public void create (String clientID, String topicName) throws JMSException {
        this.clientID = clientID;
        //подключение к activemq
       ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);

        //создание подключения
        connection = connectionFactory.createConnection();
        connection.setClientID(clientID);

        //создание сессии
        session = connection.createSession(false ,Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(topicName);

        messageProducer = session.createProducer(topic);
        System.out.println();
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public void sendName(String firstName,String lastname) throws JMSException {
        String text = firstName +" "+lastname;

        TextMessage textMessage = session.createTextMessage(text);
        messageProducer.send(textMessage);
        LOGGER.debug(clientID+ ":sent message with text = '{}'",text);
    }
}
