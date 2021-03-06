/**
 * Copyright 2020 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.kafka.da.application.config.messaging;

import javax.jms.ConnectionFactory;
import javax.net.ssl.SSLException;

import org.opensmartgridplatform.adapter.ws.infra.jms.LoggingMessageSender;
import org.opensmartgridplatform.shared.application.config.messaging.DefaultJmsConfiguration;
import org.opensmartgridplatform.shared.application.config.messaging.JmsConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;

/**
 * Configuration class for outbound requests to OSGP Logging.
 */
@Configuration
@ComponentScan(basePackageClasses = LoggingMessageSender.class)
public class OutboundLoggingRequestsMessagingConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundLoggingRequestsMessagingConfig.class);

    private final JmsConfigurationFactory jmsConfigurationFactory;

    public OutboundLoggingRequestsMessagingConfig(final Environment environment,
            final DefaultJmsConfiguration defaultJmsConfiguration) throws SSLException {
        this.jmsConfigurationFactory = new JmsConfigurationFactory(environment, defaultJmsConfiguration,
                "jms.outbound.logging.requests");
    }

    @Bean(destroyMethod = "stop", name = "kafkaDistributionAutomationOutboundLoggingRequestsConnectionFactory")
    public ConnectionFactory connectionFactory() {
        LOGGER.info("Initializing kafkaDistributionAutomationOutboundLoggingRequestsConnectionFactory bean.");
        return this.jmsConfigurationFactory.getPooledConnectionFactory();
    }

    @Bean(name = "loggingJmsTemplate")
    public JmsTemplate jmsTemplate() {
        return this.jmsConfigurationFactory.initJmsTemplate();
    }
}
