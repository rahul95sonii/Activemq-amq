package com.simple.activemq.util;

public interface AppConstants {

	String ENV_ACTIVEMQ_USER = "ACTIVEMQ_USER";
	String ENV_ACTIVEMQ_PASSWORD = "ACTIVEMQ_PASSWORD";
	String ENV_ACTIVEMQ_HOST = "ACTIVEMQ_HOST";
	String ENV_ACTIVEMQ_PORT = "ACTIVEMQ_PORT";

	String ACTIVEMQ_USER = "admin";
	String ACTIVEMQ_PASSWORD = "password";
	String ACTIVEMQ_HOST = "ssl://localhost";
	String ACTIVEMQ_PORT = "5672";

	String TOPIC_PREFIX = "topic://";
	String AMQP_PREFIX = "amqp://";
	String QUEUE_PREFIX = "queue://";
	String QUEUE_NAME = "simpragma";
}