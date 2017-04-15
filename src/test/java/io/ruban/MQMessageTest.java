package io.ruban;

import io.ruban.config.SpringRootConfig;
import io.ruban.service.mq.MQMessageSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.jms.TextMessage;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * This class provides testing sending & listening messages from Websphere MQ
 *
 * @author Andrei Ruban - software engineer.
 * @version 22.05.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringRootConfig.class})
public class MQMessageTest {

    /**
     * Message count fot test
     */
    private static final int MESSAGE_COUNT = 25;

    /**
     * Setting Environment for getting properties
     */
    @Autowired
    private Environment environment;

    /**
     * MQMessageSender for sending messages
     */
    @Autowired
    private MQMessageSender mqMessageSender;

    /**
     * JmsTemplate of Spring JMS
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void testEqualsOfSentAndReceivedMessages() {

        /*Sending messages to Queue*/
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            mqMessageSender.send("Message with" + System.nanoTime());
        }

        /*Container for received messages*/
        Set<TextMessage> messages = new HashSet<TextMessage>();

        /*Receiving messages from report Queue*/
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            TextMessage message = (TextMessage) jmsTemplate.receive(environment.getRequiredProperty("wmq.queue.test.input.2"));
            messages.add(message);
        }

        assertEquals(messages.size(), MESSAGE_COUNT);
    }
}