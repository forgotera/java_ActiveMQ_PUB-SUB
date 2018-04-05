import javax.jms.*;
import java.util.Date;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

public class Publisher {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Publisher.class);

    private String clientId;
    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    /**
     *
     * @param clientId - идентификатор клиента
     * @param topicName - название темы ссобщения
     * @throws JMSException
     */
    public void create(String clientId, String topicName)
            throws JMSException {
        this.clientId = clientId;


        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);


        connection = connectionFactory.createConnection();
        connection.setClientID(clientId);

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(topicName);

        messageProducer = session.createProducer(topic);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    /**
     * метод приветсвия пользователя
     * @param firstName - имя публициста
     * @param lastName - фимилия публициста
     * @throws JMSException
     */
    public void sendName(String firstName, String lastName) throws JMSException {
        String text = firstName + " " + lastName;

        TextMessage textMessage = session.createTextMessage(text);

        messageProducer.send(textMessage);

        LOGGER.debug(clientId + ": sent message with text='{}'", text);
    }
}
