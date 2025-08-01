package com.example.pushnotification.constants;

/**
 * Configuration class constants
 *
 * @author Ramji
 */
public final class ConfigConstants {
    // @Value constants
    public static final String BOOTSTRAP_SERVERS_PROPERTY = "${spring.kafka.bootstrap-servers}";
    public static final String EVENT_HUB_CONNECTION_STRING_PROPERTY = "${spring.kafka.properties.sasl.jaas.config}";
    public static final String SECURITY_PROTOCOL_PROPERTY = "${spring.kafka.properties.security.protocol}";
    public static final String SASL_MECHANISM_PROPERTY = "${spring.kafka.properties.sasl.mechanism}";
    public static final String HUB_NAME_PROPERTY = "${spring.kafka.topics.dp-name}";

    public static final String SECURITY_PROTOCOL = "security.protocol";

    public static final String COSMOS_URI_PROPERTY = "${azure.cosmos.uri}";
    public static final String COSMOS_KEY_PROPERTY = "${azure.cosmos.key}";
    public static final String COSMOS_DATABASE_PROPERTY = "${azure.cosmos.dbname}";
    public static final String QUERY_METRICS_ENABLED_PROPERTY = "${azure.cosmos.queryMetricsEnabled}";

    public static final String SOMETHING_WENT_WRONG = "Something went wrong. Please contact admin";

    private ConfigConstants() {
        throw new IllegalStateException(
                "ConfigConstants class which cannot be instantiated directly!!");
    }
}