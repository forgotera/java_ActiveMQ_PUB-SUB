import logic.DurableSubscriber;
import logic.Publisher;
import logic.Subscriber;

import javax.jms.JMSException;

public class main {
    public static void main(String[] args) throws JMSException {
        Publisher publisher = new Publisher();
        Subscriber subscriber = new Subscriber();
        //DurableSubscriber durableSubscriber = new DurableSubscriber();
        try {
            publisher.create("1", "12");
            subscriber.create("2", "12");
            publisher.sendName("test", "testovich");
            subscriber.getGreeting(1000);
            //durableSubscriber.create("3","12","ttest");
            // durableSubscriber.getGreeting(1000);

            publisher.closeConnection();
            // durableSubscriber.removeDurableSubscriber();
            //durableSubscriber.close();
            subscriber.closeConnection();
        }catch (Exception e){
            publisher.closeConnection();
            subscriber.closeConnection();
        }
    }
}
