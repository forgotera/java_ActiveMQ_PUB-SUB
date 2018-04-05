import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jms.JMSException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class SubscriberTest {
    private static Publisher publisher1,publisher2;
    private static Subscriber subscriber1,subscriber2;

    @BeforeClass
    public  static void setUpBeforeClass() throws Exception {
        publisher1 = new Publisher();
        publisher1.create("publisher1","test1");
        subscriber1 = new Subscriber();
        subscriber1.create("subscriber1","test1");
        subscriber2 = new Subscriber();
        subscriber2.create("subscriber12","test1");


    }

    /**
     * отправка и прием сообщения.
     */
    @Test
    public void testGetGreeting() {
        try {
            publisher1.sendName("Peregrin", "Took");

            String greeting1 = subscriber1.getGreeting(1000);
            assertEquals("Hello Peregrin Took!", greeting1);

            String greeting2 = subscriber1.getGreeting(1000);
            assertEquals("no greeting", greeting2);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }
    /**
     * два подписчика видят сообщения одной темы
    */
    @Test
    public void testMultipleComsumer(){
        try {
            publisher1.sendName("harry","potter");

            String greeting1 = subscriber1.getGreeting(1000);
            assertEquals("Hello harry potter!",greeting1);

            String greeting2 = subscriber2.getGreeting(1000);
            assertEquals("Hello harry potter!",greeting2);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    /**отправляется сообщениее,подключается второй польователь,отправляется еще сообщение
     * результат: первый юзер видит 2 сообщ
     *            второй юзер видит 1 сообщ
    */
    @Test
    public void testNonDurableSubc(){
        try {
            subscriber2.closeConnection();
            publisher1.sendName("Harry","Potter");
            subscriber2.create("4","test1");
            publisher1.sendName("steven","king");

            String greeting1 = subscriber1.getGreeting(1000);
            assertEquals("Hello Harry Potter!",greeting1);

            String greeting2 = subscriber1.getGreeting(1000);
            assertEquals("Hello steven king!",greeting2);

            String greeting3 = subscriber2.getGreeting(1000);
            assertEquals("Hello steven king!",greeting3);

            String greeting4 = subscriber2.getGreeting(1000);
            assertEquals("no greeting",greeting4);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
