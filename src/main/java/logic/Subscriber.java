package logic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 *Временная подписка
 * сообщенея доходят до юзера
 * только при его подлючении
 */
public class Subscriber {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Subscriber.class);

    private static final String NO_GREETING = "no greeting";

    private String clientId;
    private Connection connection;
    private MessageConsumer messageConsumer;

    /**
     *
     * @param clientId - идентификатор подписчика
     * @param topicName - название темы которую слушает подписчик
     * @throws JMSException
     */
    public void create(String clientId, String topicName) throws JMSException {
        this.clientId = clientId;

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);

        connection = connectionFactory.createConnection();
        connection.setClientID(clientId);

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(topicName);

        messageConsumer = session.createConsumer(topic);

        connection.start();
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    /**
     *
     * @param timeout - время ожидания сообщения от публициста
     * @return - приветсвие публициста
     * @throws JMSException
     */
    public String getGreeting(int timeout) throws JMSException {

        String greeting = NO_GREETING;


        Message message = messageConsumer.receive(timeout);

        if (message != null) {
            TextMessage textMessage = (TextMessage) message;

            String text = textMessage.getText();
            LOGGER.debug(clientId + ": received message with text='{}'",
                    text);

            greeting = "Hello " + text + "!";
        } else {
            LOGGER.debug(clientId + ": no message received");
        }

        LOGGER.info("greeting={}", greeting);
        return greeting;
    }
}
