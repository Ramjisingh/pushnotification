package com.example.pushnotification.util;

import static com.example.pushnotification.constants.ApplicationConstants.TRACE_ID;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

/**
 * Utility class providing helper methods for header processing.
 *
 * <p>This class is intended to extract and transform HTTP headers into a format suitable for Kafka
 * message headers.
 */
public class AppUtil {

    private AppUtil() {
        throw new IllegalStateException("AppUtil class which cannot be instantiated directly!!");
    }

    /**
     * Converts {@link HttpHeaders} to a list of Kafka {@link Header} objects using UTF-8 encoding
     *
     * @param httpHeaders the HTTP headers to convert
     * @return list of Kafka headers
     */
    public static List<Header> fetchHeaderValue(HttpHeaders httpHeaders) {
        List<Header> headers =
                httpHeaders.entrySet().stream()
                        .flatMap(
                                entry ->
                                        entry.getValue().stream()
                                                .map(
                                                        value ->
                                                                new RecordHeader(
                                                                        entry.getKey(), value.getBytes(StandardCharsets.UTF_8))))
                        .collect(Collectors.toList());

        if (!httpHeaders.containsKey(TRACE_ID)) {
            headers.add(
                    new RecordHeader(
                            TRACE_ID.toLowerCase(), MDC.get(TRACE_ID).getBytes(StandardCharsets.UTF_8)));
        }

        return Collections.unmodifiableList(headers);
    }

    /**
     * @param headers contains all keys and values and find the requiredHeader Key in headers
     * @return return Map with matched HeaderKey and HeaderValue
     */
    public static Map<String, String> requiredHeaderValidation(
            List<Header> headers, String requiredHeaderKey) {
        return headers.stream()
                .filter(header -> requiredHeaderKey.equalsIgnoreCase(header.key()))
                .collect(Collectors.toMap(Header::key, header -> new String(header.value())));
    }

    /**
     * @param headers contains all keys and values and convert To Map
     * @return return Map with all HeaderKey and HeaderValue
     */
    public static Map<String, String> kafkaHeaderToMap(List<Header> headers) {
        return headers.stream()
                .collect(
                        Collectors.toMap(
                                Header::key, header -> new String(header.value(), StandardCharsets.UTF_8)));
    }

    /**
     * @param headerMap contains all keys and values and convert To KafkaHeader List
     * @return return List with Kafka Header
     */
    public static List<Header> mapToKafkaHeaders(Map<String, String> headerMap) {
        return headerMap.entrySet().stream()
                .map(
                        entry ->
                                new RecordHeader(entry.getKey(), entry.getValue().getBytes(StandardCharsets.UTF_8)))
                .collect(Collectors.toList());
    }

  /*  public static <T> List<T> readValue(String cachedValue, TypeReference<List<T>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String cleanedJson = StringEscapeUtils.unescapeJava(cachedValue);
            return objectMapper.readValue(cleanedJson, typeReference);
        } catch (JsonProcessingException e) {
            throw new DeliveryServiceException("Failed to deserialize cached JSON", e);
        }
    }

    public static String writeAsStringValue(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new DeliveryServiceException("Failed to serialize cached Object", e);
        }
    }*/
}