## Overview

# Activemq-amq
Activemq-amq SSL certification

This is an  overview of how to use the Java JMS API with ActiveMQ via the AMQP protocol.

You can control to which AMQP server try to connect to by
setting the following environment variables in AppConstants.java

* `ACTIVEMQ_HOST`
* `ACTIVEMQ_PORT`
* `ACTIVEMQ_USER`
* `ACTIVEMQ_PASSWORD`

## Running the App

* First run InstallCert.java  for Generate the SSL certificate then its working with HTTPS
* Then run Consumer.java
* then Producer.java

need to implement the ssl certificate from specific folder
