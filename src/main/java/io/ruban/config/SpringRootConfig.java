package io.ruban.config;

import io.ruban.service.mq.MQMessageListener;
import io.ruban.service.mq.MQMessageSender;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.JMSException;
import java.util.logging.Logger;

/**
 * This class provides setting up the application context
 * & describes bean definition of Spring JMS
 * & IBM Websphere MQ classes for JMS
 * <p/>
 * There are two modes of working with IBM Websphere MQ:
 * the binding mode & the client mode.
 * The two of modes are implemented.
 * <p/>
 * Configuration of IBM Websphere MQ is described in wmq.properties
 *
 * @author Andrei Ruban - software engineer.
 * @version 09.07.2015
 */

@Configuration
@ComponentScan("by.iba")
@PropertySource(value = {"classpath:wmq.properties"})
public class SpringRootConfig {

    /**
     * Logger is using for tracing status of the application configuration
     */
    public static final Logger LOGGER = Logger.getLogger(SpringRootConfig.class.getName());

    /**
     * Environment Bean for working with properties
     */
    @Autowired
    private Environment environment;

    /**
     * Method provides setting up the MQConnectionFactory Bean in Binding mode
     *
     * @return MQConnectionFactory is set up in binding mode
     * @throws JMSException if setting up MQConnectionFactory failed
     */
    @Bean
    public MQConnectionFactory mqBindingConnectionFactory() {
        MQConnectionFactory connectionFactory = new MQConnectionFactory();
        try {
            connectionFactory.setHostName(environment.getRequiredProperty("wmq.qmgr.host"));
            connectionFactory.setPort(environment.getProperty("wmq.qmgr.port", Integer.class));
            connectionFactory.setQueueManager(environment.getRequiredProperty("wmq.qmgr.name"));
            connectionFactory.setTransportType(environment.getProperty("wmq.qmgr.transport.type.binding", Integer.class));
            connectionFactory.setCCSID(environment.getProperty("wmq.qmgr.ccsid", Integer.class));
        } catch (JMSException e) {
            LOGGER.severe("Cannot set up binding connection factory" + e.getMessage());
        }
        return connectionFactory;
    }

    /**
     * Method provides setting up the MQConnectionFactory Bean in Client mode
     *
     * @return MQConnectionFactory is set up in client mode
     * @throws JMSException if setting up MQConnectionFactory failed
     */
    @Bean
    public MQConnectionFactory mqClientConnectionFactory() {
        MQConnectionFactory connectionFactory = new MQConnectionFactory();
        try {
            connectionFactory.setHostName(environment.getRequiredProperty("wmq.qmgr.host"));
            connectionFactory.setPort(environment.getProperty("wmq.qmgr.port", Integer.class));
            connectionFactory.setQueueManager(environment.getRequiredProperty("wmq.qmgr.name"));
            connectionFactory.setTransportType(environment.getProperty("wmq.qmgr.transport.type.client", Integer.class));
            connectionFactory.setCCSID(environment.getProperty("wmq.qmgr.ccsid", Integer.class));
            connectionFactory.setChannel(environment.getRequiredProperty("wmq.qmgr.channel"));
        } catch (JMSException je) {
            LOGGER.severe("Cannot set up client connection factory" + je.getMessage());
        }
        return connectionFactory;
    }

    /**
     * Method provides setting up Single Connection Factory Bean for using in Binding mode
     *
     * @return SingleConnectionFactory
     */
    @Bean
    public SingleConnectionFactory jmsQueueConnectionFactory() {
        SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory();
        singleConnectionFactory.setTargetConnectionFactory(mqBindingConnectionFactory());
        singleConnectionFactory.setReconnectOnException(true);
        return singleConnectionFactory;
    }

    /**
     * Method provides setting up UserCredentialsConnectionFactoryAdapter Bean for using in Client mode
     *
     * @return UserCredentialsConnectionFactoryAdapter
     */
    @Bean
    public UserCredentialsConnectionFactoryAdapter jmsQueueConnectionFactorySecured() {
        UserCredentialsConnectionFactoryAdapter connectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
        connectionFactoryAdapter.setTargetConnectionFactory(mqClientConnectionFactory());
        connectionFactoryAdapter.setUsername(environment.getRequiredProperty("wmq.qmgr.username"));
        connectionFactoryAdapter.setPassword(environment.getRequiredProperty("wmq.qmgr.password"));
        return connectionFactoryAdapter;
    }

    /**
     * Method provides setting up CachingConnectionFactory Bean to organize connection pool
     *
     * @return CachingConnectionFactory
     */
    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
//        cachingConnectionFactory.setTargetConnectionFactory(jmsQueueConnectionFactory());
        cachingConnectionFactory.setTargetConnectionFactory(jmsQueueConnectionFactorySecured());
        cachingConnectionFactory.setCacheConsumers(true);
        cachingConnectionFactory.setCacheProducers(true);
        cachingConnectionFactory.setSessionCacheSize(5);
        return cachingConnectionFactory;
    }

    /**
     * Method provides setting up DynamicDestinationResolver Bean
     *
     * @return DynamicDestinationResolver
     */
    @Bean
    public DynamicDestinationResolver destinationResolver() {
        return new DynamicDestinationResolver();
    }

    /**
     * Method provides setting up JmsTemplate Bean
     *
     * @return DynamicDestinationResolver
     */
    @Bean
    public JmsTemplate jmsQueueTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(jmsQueueConnectionFactorySecured()); // optionally switching client mode
//        jmsTemplate.setConnectionFactory(jmsQueueConnectionFactory()); // optionally switching binding mode
        jmsTemplate.setDestinationResolver(destinationResolver());
        jmsTemplate.setReceiveTimeout(10000);
        return jmsTemplate;
    }

    /**
     * Method provides setting up MQMessageListener Bean for getting messages
     * from Websphere MQ and his personal settings
     *
     * @return MQMessageListener
     */
    @Bean
    public MQMessageListener mqMessageListener() {
        return new MQMessageListener();
    }

    /**
     * Method provides MQMessageSender Bean for sending messages
     * to Websphere MQ and his personal settings
     *
     * @return @MQMessageSender
     */
    @Bean
    public MQMessageSender mqMessageSender() {
        MQMessageSender mqMessageSender = new MQMessageSender();
        mqMessageSender.setDestinationQueue(environment.getRequiredProperty("wmq.queue.test.input.1"));
        mqMessageSender.setForwardQueue(environment.getRequiredProperty("wmq.queue.test.input.2"));
        return mqMessageSender;
    }

    /**
     * Method provides DefaultMessageListenerContainer Bean for sending messages
     *
     * @return DefaultMessageListenerContainer
     */
    @Bean
    public DefaultMessageListenerContainer jmsListenerContainer() {
        DefaultMessageListenerContainer dmlc = new DefaultMessageListenerContainer();
        try {
            dmlc.setConnectionFactory(mqClientConnectionFactory()); // optionally switching client mode
//            dmlc.setConnectionFactory(mqBindingConnectionFactory()); // optionally switching binding mode
            dmlc.setConcurrentConsumers(environment.getProperty("wmq.listener.concurrent.consumers", Integer.class));
            dmlc.setMessageListener(mqMessageListener());
            dmlc.setDestination(new MQDestination(environment.getRequiredProperty("wmq.queue.test.input.1")));
        } catch (JMSException je) {
            LOGGER.severe(je.getMessage());
        }
        return dmlc;
    }

}
