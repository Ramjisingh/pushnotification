package com.example.pushnotification.controller;

import com.example.pushnotification.azure.event.hub.model.DeviceLogonRequest;
import com.example.pushnotification.config.openapi.DeviceLogonOperation;
import com.example.pushnotification.service.EventHubProducerService;
import com.example.pushnotification.util.AppUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.pushnotification.constants.ApplicationConstants.DEVICE_ID;
import static com.example.pushnotification.constants.ApplicationConstants.DEVICE_ID_MISSING_IN_HEADER;

/**
 * REST controller for MWI device operations.
 *
 * @author Ramji Yadav
 */
@Tag(name = "MWI Device Operations", description = "MWI Device operation API's")
@RestController
@RequestMapping("/device")
@Log4j2
@RequiredArgsConstructor
//@SecurityRequirement(name = "bearerAuth")
//@CrossOrigin
public class DeviceOperationController {

  private final EventHubProducerService eventHubProducerService;

 // @Value("${spring.kafka.topics.logon-name}")
  String logonTopic="logtopic";

  /**
   * This endpoint is will be used to send logon request to host system.
   *
   * @param deviceLogonRequest The Logon request containing the user logon information.
   * @return A response indicating we have successfully sent logon request (HTTP 202 Accepted).
   */
  @DeviceLogonOperation
  @PutMapping(value = "/logon/v1", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deviceLogon(
      @RequestHeader HttpHeaders httpHeaders,
      @RequestBody @Valid DeviceLogonRequest deviceLogonRequest) {
    List<Header> headers = AppUtil.fetchHeaderValue(httpHeaders);
    Map<String, String> matchedHeader = AppUtil.requiredHeaderValidation(headers, DEVICE_ID);
    if (matchedHeader.isEmpty() || matchedHeader.get(DEVICE_ID).isBlank()) {
      throw new IllegalArgumentException(DEVICE_ID, new Throwable(DEVICE_ID_MISSING_IN_HEADER));
    }

    eventHubProducerService.sendMessage(
            deviceLogonRequest.getRoundId(), deviceLogonRequest, headers, logonTopic);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }
}
