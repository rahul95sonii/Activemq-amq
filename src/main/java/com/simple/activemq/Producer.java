package com.simple.activemq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.simple.activemq.util.AppConstants;
import com.simple.activemq.util.AppUtil;
import org.apache.log4j.Logger;
import org.apache.qpid.jms.JmsConnectionFactory;

/**
 * Producer class which helps in sending messages to a queue
 * 
 * @author Rahul
 */
public class Producer implements MessageListener, AppConstants {

	private static final Logger LOG = Logger.getLogger(Producer.class.getName());
	private Map<String, TextMessage> idMessageMap = new HashMap<String, TextMessage>();

	public static void main(String[] args) throws Exception {
		new Producer(args);
	}

	/**
	 * Inits application and starts sending messages to queue
	 * 
	 * @param args
	 *            {@link String[]} args
	 * @throws JMSException
	 */
	public Producer(String[] args) throws Exception {
		String user = AppUtil.env(ENV_ACTIVEMQ_USER, ACTIVEMQ_USER);
		String password = AppUtil.env(ENV_ACTIVEMQ_PASSWORD, ACTIVEMQ_PASSWORD);
		String host = AppUtil.env(ENV_ACTIVEMQ_HOST, ACTIVEMQ_HOST);
		int port = Integer.parseInt(AppUtil.env(ENV_ACTIVEMQ_PORT, ACTIVEMQ_PORT));

		String connectionURI = AMQP_PREFIX + host + ":" + port;
		//String connectionURI = "AMQP_PREFIX//host/?brokerlist='tcp://host:port?ssl='true'&key_store='C:/apache-activemq-5.8.0/conf/client.ks'&key_store_password='password'&trust_store='C:/apache-activemq-5.8.0/conf/client.ts'&trust_store_password='password'";
		String destinationName = AppUtil.arg(args, 0, QUEUE_PREFIX + QUEUE_NAME);

		JmsConnectionFactory factory = new JmsConnectionFactory(connectionURI);

		Connection connection = factory.createConnection(user, password);
		connection.start();

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Destination destination = null;
		if (destinationName.startsWith(TOPIC_PREFIX)) {
			destination = session.createTopic(destinationName.substring(TOPIC_PREFIX.length()));
		} else {
			destination = session.createQueue(destinationName);
		}

		MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		Destination tempDest = session.createTemporaryQueue();
		MessageConsumer responseConsumer = session.createConsumer(tempDest);
		responseConsumer.setMessageListener(this);

		// Hardcoded to send 5 messages
		int messages = 5;
		for (int i = 1; i <= messages; i++) {
			TextMessage msg = session.createTextMessage("Hello - " + i);
			msg.setJMSReplyTo(tempDest);
			String correlationId = AppUtil.createRandomString();
			msg.setJMSCorrelationID(correlationId);
			msg.setIntProperty("id", i);
			LOG.debug("Sending message:-\nCorrelation ID: " + correlationId);
			LOG.debug("Text: " + msg.getText());
			LOG.debug("Reply To: " + msg.getJMSReplyTo() + "\n");
			idMessageMap.put(correlationId, msg);
			producer.send(msg);
			Thread.sleep(5);
		}
	}

	@Override
	public void onMessage(Message message) {
		String messageText = null;
		try {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				messageText = textMessage.getText();
				LOG.debug("Received response from consumer: " + messageText);
				if (idMessageMap.get(textMessage.getJMSCorrelationID()) != null) {
					LOG.debug("Received response for Correlation ID: " + textMessage.getJMSCorrelationID() + "\n");
				}
			}
		} catch (JMSException e) {
			LOG.error("Error while reading response message: ", e);
		}
	}
}