package com.example.pushnotification.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static com.example.pushnotification.constants.ConfigConstants.BOOTSTRAP_SERVERS_PROPERTY;
import static com.example.pushnotification.constants.ConfigConstants.SECURITY_PROTOCOL;
import static com.example.pushnotification.constants.ConfigConstants.EVENT_HUB_CONNECTION_STRING_PROPERTY;
import static com.example.pushnotification.constants.ConfigConstants.SECURITY_PROTOCOL_PROPERTY;
import static com.example.pushnotification.constants.ConfigConstants.SASL_MECHANISM_PROPERTY;

import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.common.config.SaslConfigs.SASL_JAAS_CONFIG;
import static org.apache.kafka.common.config.SaslConfigs.SASL_MECHANISM;

/**
 * Configuration class for Kafka producer.
 *
 * @author Ramji yadav
 */
@Configuration
public class EventHubProducerConfig {
    @Value(BOOTSTRAP_SERVERS_PROPERTY)
    private String bootstrapServers;

    @Value(EVENT_HUB_CONNECTION_STRING_PROPERTY)
    private String eventHubConnectionString;

    @Value(SECURITY_PROTOCOL_PROPERTY)
    private String securityProtocol;

    @Value(SASL_MECHANISM_PROPERTY)
    private String saslMechanism;

    /**
     * Creates a producer factory with the specified configuration properties.
     *
     * @return a configured producer factory
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ACKS_CONFIG, "all");
        configProps.put(RETRIES_CONFIG, "5");
        configProps.put(SECURITY_PROTOCOL, securityProtocol);
        configProps.put(SASL_MECHANISM, saslMechanism);
        configProps.put(SASL_JAAS_CONFIG, eventHubConnectionString);

        configProps.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates a KafkaTemplate with the configured producer factory.
     *
     * @return a configured KafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}