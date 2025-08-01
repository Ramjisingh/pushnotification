package com.example.pushnotification.controller;

import com.example.pushnotification.model.*;
import com.example.pushnotification.service.PushNotificationService;
import com.example.pushnotification.util.AppUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fedex")
@Slf4j
public class PushNotifictionController {

  private static final Logger logger = LoggerFactory.getLogger(PushNotifictionController.class);

  @Autowired private final PushNotificationService pushNotificationService;

  public PushNotifictionController(PushNotificationService pushNotificationService) {
    this.pushNotificationService = pushNotificationService;
  }

  /*@PostMapping(value = "/json-to-xml", produces = MediaType.APPLICATION_XML_VALUE)
  public ResponseEntity<IBISResponse> getLoginXmlData(@RequestBody IBISRequest iBISRequest) throws JAXBException, IOException {

      logger.info("Received request: {}", iBISRequest);

      IBISResponse xmlString = Utility.marshalToXml(iBISRequest);
      return new ResponseEntity<IBISResponse>(xmlString, HttpStatus.OK);

  }*/

  @PostMapping("/notification/token")
  public ResponseEntity<Response<PushNotificationResponse>> sendTokenNotification(
      @RequestHeader HttpHeaders httpHeaders, @RequestBody PushNotificationRequest request) {

    // log.info("Received request to send notification: {}", request);
    List<Header> headers = AppUtil.fetchHeaderValue(httpHeaders);
    pushNotificationService.sendPushNotificationToToken(headers, request);

    PushNotificationResponse response =
        new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent.");
    Response<PushNotificationResponse> standardizedResponse =
        new Response<>(true, "Success", response);

    return new ResponseEntity<>(standardizedResponse, HttpStatus.OK);

    // return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification
    // has been sent."), HttpStatus.OK);
  }

  /* @PostMapping("/notification/token")
  public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
      pushNotificationService.sendPushNotificationToToken(request);
      System.out.println("princr");
      return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
  }*/

}
