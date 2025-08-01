package com.example.pushnotification.service;

import com.example.pushnotification.model.PushNotificationRequest;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushNotificationService {

  private final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

  @Autowired private FCMService fcmService;

  /*    public PushNotificationService(FCMService fcmService) {
      this.fcmService = fcmService;
  }*/

  public void sendPushNotificationToToken(List<Header> headers, PushNotificationRequest request) {

    try {
      fcmService.sendMessageToToken(request);

    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
