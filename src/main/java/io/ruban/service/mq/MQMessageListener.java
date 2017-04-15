package io.ruban.service.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * This class provides getting messages from Websphere MQ
 *
 * @author Andrei Ruban - software engineer.
 * @version 19.05.2015
 */
public class MQMessageListener implements MessageListener {

    /**
     * Logger is using for tracing status of the MQMessageListener
     */
    private static final Logger LOGGER = Logger.getLogger(MQMessageListener.class.getName());

    /**
     * JmsTemplate of Spring JMS
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Method provides getting messages from Queue of Websphere MQ
     *
     * @param message
     * @throws JMSException
     */
    @Override
    public void onMessage(Message message) {

        try {
            final String messageId = message.getJMSMessageID();
            LOGGER.info(
                    format(
                            "\n%s got message %s\nType: %s",
                            Thread.currentThread().getName(),
                            messageId,
                            message.getClass().getName()
                    )
            );

            if (isTextMessage(message)) {
                //Process text message
                final TextMessage textMessage = (TextMessage) message;
                final String payload = textMessage.getText();

                LOGGER.info(format(
                        "\nPayload:\n>%s...<",
                        payload.substring(0, payload.length() > 50 ? 50 : payload.length())
                ));

                final Destination replyTo = message.getJMSReplyTo();

                if (replyTo == null) {
                    LOGGER.warning(format("Message %s comes without JMSReplyTo", messageId));
                } else {
                    LOGGER.info("Reply to" + replyTo);
                    final AtomicReference<TextMessage> resposentRef = new AtomicReference<TextMessage>();
                    jmsTemplate.send(replyTo, new MessageCreator() {

                        public Message createMessage(Session session) throws JMSException {
                            final TextMessage response = session.createTextMessage();
                            response.setJMSCorrelationID(messageId);
                            response.setText("Ok");
                            resposentRef.set(response);
                            return response;
                        }
                    });
                    LOGGER.info(format(
                            "Sent reply for %s with Message=%s",
                            messageId,
                            resposentRef.get().getJMSMessageID()
                    ));
                }

            } else {
                final String wrongTypeMessage = "We don't handle messages another type message type. Only TextMessage";
                LOGGER.warning(wrongTypeMessage);
                throw new JMSException(wrongTypeMessage);
            }

        } catch (JMSException je) {
            LOGGER.severe(je.getMessage());
            //TODO putting to Dead Letter Queue or wherever
        }
    }

    /**
     * Checks if message of type of TextMessage
     *
     * @param message
     * @return boolean: true if message of TextMessage
     */
    public boolean isTextMessage(Message message) {
        return message instanceof TextMessage;
    }
}
