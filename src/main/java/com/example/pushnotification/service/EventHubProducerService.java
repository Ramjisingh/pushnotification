package com.example.pushnotification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fedex.mwi.delivery.service.entity.EventHubFailedMessages;
//import com.fedex.mwi.delivery.service.repository.EventHubFailedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for producing Kafka messages.
 *
 * @author Ramji
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class EventHubProducerService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

 // private final EventHubFailedMessageRepository failedMessageRepository;

  private final ObjectMapper objectMapper;

  /**
   * Sends a message to the specified Kafka topic using CompletableFuture. Logs success or failure
   * of the message send.
   *
   * @param message the message to be sent
   */
  public void sendMessage(String key, Object message, List<Header> headers, String topic) {

    ProducerRecord<String, Object> producerRecord =
        new ProducerRecord<>(topic, null, key, message, headers);
    kafkaTemplate
        .send(producerRecord)
        .whenComplete(
            (result, ex) -> {
              if (ex == null && result != null) {
                log.info(
                    "Sent message: {}, with offset {}",
                    message,
                    result.getRecordMetadata().offset());
              } else {
                log.error("Something went wrong here!! {}", ex.getMessage());
                //saveToCosmosDb(message, key);
              }
            });
  }

  /**
   * This method used to save the events to cosmos DB, when it's failed to publish to eventhub
   */
/*  private void saveToCosmosDb(Object payload, String key) {
    EventHubFailedMessages failedMessage =
        EventHubFailedMessages.builder()
            .id(UUID.randomUUID().toString())
            .payload(payload)
            .key(key)
            .build();
    failedMessageRepository.save(failedMessage);
    log.info("Saved failed message to COSMOS DB: {}", failedMessage);
  }*/

  /**
   * Attempts to retry sending failed messages from cosmos DB to Eventhub.
   */
  /*public void retryFailedMessages(String topic) {
    List<EventHubFailedMessages> failedMessages =
        (List<EventHubFailedMessages>) failedMessageRepository.findAll();

    for (EventHubFailedMessages failedMessage : failedMessages) {
      try {
        kafkaTemplate
            .send(topic, failedMessage.getKey(), failedMessage.getPayload())
            .whenComplete(
                (result, ex) -> {
                  if (ex == null) {
                    failedMessageRepository.delete(failedMessage);
                    log.info("Successfully retried messages: {}", failedMessage.getId());
                  } else {
                    log.error("Failed to retry message: {}", failedMessage.getId(), ex);
                  }
                });
      } catch (Exception e) {
        log.error("Error retrying message: {}", failedMessage.getId(), e);
      }
    }
  }*/
}