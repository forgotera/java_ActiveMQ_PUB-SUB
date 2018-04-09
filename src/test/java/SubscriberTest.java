import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jms.JMSException;

import logic.DurableSubscriber;
import logic.Publisher;
import logic.Subscriber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class SubscriberTest {
    private static Publisher publisher1,publisher2,publisher3,testNonDurablepubl,durableSubcriberTestPublisher;
    private static Subscriber subscriber1,subscriber2,testMultipleComsumersubscriber1,testMultipleComsumersubscriber2,testNonDurableSubc1,testNonDurableSubc2,
            durableSubcriberTestSubcriber1,durableSubcriberTestSubcriber2;
    private  static DurableSubscriber durableSubcriberTestPublisher1,durableSubcriberTestPublisher2;

    @BeforeClass
    public  static void setUpBeforeClass() throws Exception {
        publisher1 = new Publisher();
        publisher1.create("publisher1","test1");
        publisher2 = new Publisher();
        publisher2.create("publisher2","test2");
        publisher3 = new Publisher();
        publisher3.create("publisher3","test3");
        testNonDurablepubl = new Publisher();
        testNonDurablepubl.create("testNonDurablePub","test4");
        durableSubcriberTestPublisher = new Publisher();
        durableSubcriberTestPublisher.create("durableSubcriberTestPublisher","test5");

        subscriber1 = new Subscriber();
        subscriber1.create("subscriber1","test1");
        subscriber2 = new Subscriber();
        subscriber2.create("subscriber2","test2");
        testMultipleComsumersubscriber1 = new Subscriber();
        testMultipleComsumersubscriber1.create("subcriber3","test3");
        testMultipleComsumersubscriber2 = new Subscriber();
        testMultipleComsumersubscriber2.create("subscriberTest3","test3");
        testNonDurableSubc1 = new Subscriber();
        testNonDurableSubc1.create("testNonDurableSubc1","test4");
        testNonDurableSubc2 = new Subscriber();
        testNonDurableSubc2.create("testNonDurableSubc2","test4");
        durableSubcriberTestSubcriber1 = new Subscriber();
        durableSubcriberTestSubcriber1.create(" durableSubcriberTestSubcriber1","test5");
        durableSubcriberTestSubcriber2 = new Subscriber();
        durableSubcriberTestSubcriber2.create(" durableSubcriberTestSubcriber2","test5");


        durableSubcriberTestPublisher1 = new DurableSubscriber();
        durableSubcriberTestPublisher1.create(
                "durableSubsriber1", "test5",
                "t1");
        durableSubcriberTestPublisher2 = new DurableSubscriber();
        durableSubcriberTestPublisher2.create(
                "durableSubsriber2", "test5",
                "t2");
    }

    @AfterClass
    public  static  void tearDownAfterClass() throws JMSException {
        publisher1.closeConnection();
        publisher2.closeConnection();
        publisher3.closeConnection();
        testMultipleComsumersubscriber1.closeConnection();
        testMultipleComsumersubscriber2.closeConnection();
        durableSubcriberTestPublisher.closeConnection();

        subscriber1.closeConnection();
        subscriber2.closeConnection();
        subscriber2.closeConnection();
        testMultipleComsumersubscriber1.closeConnection();
        testMultipleComsumersubscriber2.closeConnection();
        testNonDurableSubc1.closeConnection();
        testNonDurableSubc2.closeConnection();
        durableSubcriberTestSubcriber1.closeConnection();
        durableSubcriberTestSubcriber1.closeConnection();

        durableSubcriberTestPublisher1.removeDurableSubscriber();
        durableSubcriberTestPublisher1.close();
        durableSubcriberTestPublisher2.removeDurableSubscriber();
        durableSubcriberTestPublisher2.close();
    }

    /**
     * отправка и прием сообщения.
     */
    @Test
    public void testGetGreeting() {
        try {
            publisher2.sendName("Peregrin", "Took");

            String greeting1 = subscriber2.getGreeting(1000);
            assertEquals("Hello Peregrin Took!", greeting1);

            String greeting2 = subscriber2.getGreeting(1000);
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
            publisher3.sendName("harry","potter");

            String greeting1 = testMultipleComsumersubscriber1.getGreeting(1000);
            assertEquals("Hello harry potter!",greeting1);

            String greeting2 = testMultipleComsumersubscriber2.getGreeting(1000);
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
            testNonDurableSubc2.closeConnection();
            testNonDurablepubl.sendName("Harry","Potter");
            testNonDurableSubc2.create("testNonDurableSubc2","test4");
            testNonDurablepubl.sendName("steven","king");

            String greeting1 = testNonDurableSubc1.getGreeting(1000);
            assertEquals("Hello Harry Potter!",greeting1);

            String greeting2 = testNonDurableSubc1.getGreeting(1000);
            assertEquals("Hello steven king!",greeting2);

            String greeting3 = testNonDurableSubc2.getGreeting(1000);
            assertEquals("Hello steven king!",greeting3);

            String greeting4 = testNonDurableSubc2.getGreeting(1000);
            assertEquals("no greeting",greeting4);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * проверка длительной подписки
     * первый юзер подключаются до отправки сообщений
     * второй юзер подключается после отправки сообщений
     * результат: оба получают все сообщения
     * @throws JMSException
     */
    @Test
    public  void durableSubcriberTest() throws JMSException {
        durableSubcriberTestPublisher1.close();
        durableSubcriberTestPublisher.sendName("ivan","ivanovich");
        durableSubcriberTestPublisher1.create("durableSubcriberTestPublisher1","test1","durableSubscriber");
        durableSubcriberTestPublisher.sendName("holl","sidorov");

        String greeting1 = durableSubcriberTestSubcriber2.getGreeting(1000);
        assertEquals("Hello ivan ivanovich!",greeting1);

        String greeting2 = durableSubcriberTestSubcriber1.getGreeting(1000);
        assertEquals("Hello ivan ivanovich!",greeting2);

        String greeting3 = durableSubcriberTestSubcriber2.getGreeting(1000);
        assertEquals("Hello holl sidorov!",greeting3);

        String greeting4 = durableSubcriberTestSubcriber1.getGreeting(1000);
        assertEquals("Hello holl sidorov!",greeting4);
    }

}
