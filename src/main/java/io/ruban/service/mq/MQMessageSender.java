package io.ruban.service.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * This class provides sending messages to Websphere MQ
 *
 * @author Andrei Ruban - software engineer.
 * @version 19.05.2015
 */
public class MQMessageSender {

    /**
     * JmsTemplate of Spring JMS
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Destination queue in Webspere MQ
     */
    private String destinationQueue;

    /**
     * queue in Webspere MQ which is used by listener as destination in forwarding
     */
    private String forwardQueue;

    public String getDestinationQueue() {
        return destinationQueue;
    }

    public void setDestinationQueue(String destinationQueue) {
        this.destinationQueue = destinationQueue;
    }

    public String getForwardQueue() {
        return forwardQueue;
    }

    public void setForwardQueue(String forwardQueue) {
        this.forwardQueue = forwardQueue;
    }

    /**
     * Sends message to Queue of Websphere MQ. Destination queue is specified in property file.
     * Encapsulates @method send(final String message, final String queue)
     *
     * @param message
     */
    public void send(final String message) {
        send(message, getDestinationQueue());
    }

    /**
     * Sends message to Queue of Websphere MQ. Both parameters are specified in the method signature
     *
     * @param message
     * @param queue
     */
    public final void send(final String message, final String queue) {
        jmsTemplate.send(queue, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage();
                tm.setText(message);
                if (isNeedReporting()) {
                    tm.setJMSReplyTo(jmsTemplate.getDestinationResolver().resolveDestinationName(session, getForwardQueue(), false));
                }
                return tm;
            }
        });
    }

    /**
     * Checks if reportQueue is defined
     *
     * @return boolean: true if reportQueue is defined
     */
    public boolean isNeedReporting() {
        return getForwardQueue() != null;
    }
}
