package com.simple.activemq;

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
 * Consumer class which helps in reading messages from a queue
 * 
 * @author Rahul
 */
public class Consumer implements MessageListener, AppConstants {

	private static final Logger LOG = Logger.getLogger(Consumer.class.getName());
	private Session session;
	private MessageProducer replyProducer;

	public static void main(String[] args) throws JMSException {
		new Consumer(args);
	}

	/**
	 * Inits application and starts listening for messages in queue
	 * 
	 * @param args
	 *            {@link String[]} args
	 * @throws JMSException
	 */
	public Consumer(String[] args) throws JMSException {
		String user = AppUtil.env(ENV_ACTIVEMQ_USER, ACTIVEMQ_USER);
		String password = AppUtil.env(ENV_ACTIVEMQ_PASSWORD, ACTIVEMQ_PASSWORD);
		String host = AppUtil.env(ENV_ACTIVEMQ_HOST, ACTIVEMQ_HOST);
		int port = Integer.parseInt(AppUtil.env(ENV_ACTIVEMQ_PORT, ACTIVEMQ_PORT));

		String connectionURI = AMQP_PREFIX + host + ":" + port;
		//String connectionURI = "AMQP_PREFIX//host/?brokerlist='tcp://host:port?ssl='true'&key_store='C:/apache-activemq-5.8.0/conf/client.ks'&key_store_password='password'&trust_store='C:/apache-activemq-5.8.0/conf/client.ts'&trust_store_password='password'";
	//	ConnectionFactoryImpl connectionFactory = ConnectionFactoryImpl.createFromURL(connectionUrl);
		String destinationName = AppUtil.arg(args, 0, QUEUE_PREFIX + QUEUE_NAME);

		JmsConnectionFactory factory = new JmsConnectionFactory(connectionURI);

		Connection connection = factory.createConnection(user, password);
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Destination destination = null;
		if (destinationName.startsWith(TOPIC_PREFIX)) {
			destination = session.createTopic(destinationName.substring(TOPIC_PREFIX.length()));
		} else {
			destination = session.createQueue(destinationName);
		}

		MessageConsumer consumer = session.createConsumer(destination);

		LOG.info("Waiting for messages...");

		this.replyProducer = this.session.createProducer(null);
		this.replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		consumer.setMessageListener(this);
	}

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage response = this.session.createTextMessage();
			if (message instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) message;
				response.setText("Reply from consumer for '" + txtMsg.getText() + "'");

				LOG.debug("Received message:-\nCorrelation ID: " + txtMsg.getJMSCorrelationID());
				LOG.debug("Text: " + txtMsg.getText());
				LOG.debug("Sending Response To: " + txtMsg.getJMSReplyTo() + "\n");
			}

			// Set the correlation ID from the received message to be the
			// correlation id of the response message
			// this lets the client identify which message this is a response to
			// if it has more than
			// one outstanding message to the server
			response.setJMSCorrelationID(message.getJMSCorrelationID());

			// Send the response to the Destination specified by the JMSReplyTo
			// field of the received message,
			// this is presumably a temporary queue created by the client
			this.replyProducer.send(message.getJMSReplyTo(), response);
		} catch (JMSException e) {
			LOG.error("Error while reading message from queue: ", e);
		}
	}
}