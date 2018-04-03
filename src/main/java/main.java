import sun.rmi.runtime.NewThreadAction;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {
    public static void main(String[] args) throws JMSException {
        Publisher publisher = new Publisher();
        Subscriber subscriber = new Subscriber();

        publisher.create("1", "12");
        subscriber.create("2", "12");
        publisher.sendName("test", "testovich");
        subscriber.getGreeting(1000);

        publisher.closeConnection();
        subscriber.closeConnection();
    }
}
