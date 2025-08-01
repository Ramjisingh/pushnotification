package com.example.pushnotification.aspect;

import static com.example.pushnotification.constants.LoggingAspectConstant.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Aspect logging for HTTP Requests & Responses in Request mapping
 *
 * @author Ramji
 */
@Log4j2
@AllArgsConstructor
@Component
@Aspect
public class ControllerLoggingAspect {

  private static final ThreadLocal<Instant> startTime = new ThreadLocal<>();
  private final ObjectMapper objectMapper;

  /** Pointcut for methods annotated with @PutMapping and @GetMapping. */
  @Pointcut(
      PUT_MAPPING_ANNOTATION
          + " || "
          + GET_MAPPING_ANNOTATION
          + " || "
          + DELETE_MAPPING_ANNOTATION
          + "||"
          + POST_MAPPING_ANNOTATION)
  public void requestMappingPointcut() {}

  /**
   * Logs the HTTP request before the execution of the target method.
   *
   * @param joinPoint the join point providing access to the target method.
   */
  @Before("requestMappingPointcut()")
  public void logHttpRequest(JoinPoint joinPoint) {
    HttpServletRequest request = getCurrentHttpRequest();
    if (request != null) {
      logRequest(joinPoint, request);
      startTime.set(Instant.now());
    }
  }

  /**
   * Logs the HTTP response after the execution of the target method.
   *
   * @param joinPoint the join point providing access to the target method.
   * @param result the result of the target method execution.
   */
  @AfterReturning(pointcut = "requestMappingPointcut()", returning = "result")
  public void logHttpResponse(JoinPoint joinPoint, Object result) {
    try {
      HttpServletRequest request = getCurrentHttpRequest();
      if (request != null) {

        logResponse(startTime.get(), request, result, joinPoint);
      }
    } finally {
      startTime.remove();
    }
  }

  /**
   * Logs any error thrown by the target method.
   *
   * @param joinPoint the join point providing access to the target method.
   * @param e the throwable error thrown by the target method.
   */
  @AfterThrowing(pointcut = "requestMappingPointcut()", throwing = "e")
  public void logHttpError(JoinPoint joinPoint, Throwable e) {
    try {
      HttpServletRequest request = getCurrentHttpRequest();
      if (request != null) {
        logError(request, e);
      }
    } finally {
      startTime.remove();
    }
  }

  /**
   * Logs the HTTP request.
   *
   * @param joinPoint the join point providing access to the target method.
   * @param request the current HTTP request.
   */
  private void logRequest(JoinPoint joinPoint, HttpServletRequest request) {
    String requestBody = getRequestBody(joinPoint);
    String headers = getRequestHeadersAsJson(request);
    withMdc(
        () ->
            log.info("Request: Method: {}, URI: {}", request.getMethod(), request.getRequestURI()),
        HTTP_INCOMING_REQUEST_URL,
        request.getRequestURI(),
        HTTP_REQUEST_METHOD,
        request.getMethod(),
        HTTP_REQUEST_HEADERS,
        headers,
        HTTP_REQUEST_BODY_CONTENT,
        requestBody);
  }

  /**
   * Logs the HTTP response.
   *
   * @param startTime the start time of the request processing.
   * @param request the current HTTP request.
   * @param result the result of the target method execution.
   * @param joinPoint the join point providing access to the target method.
   */
  private void logResponse(
      Instant startTime, HttpServletRequest request, Object result, JoinPoint joinPoint) {
    long duration = Duration.between(startTime, Instant.now()).toMillis();
    String responseBody = convertObjectToJson(result);
    int statusCode = getStatusCode(result);
    withMdc(
        () ->
            log.info(
                "Response: Method: {}, URI: {}, Status {}, {} executed in: {} ms",
                request.getMethod(),
                request.getRequestURI(),
                statusCode,
                joinPoint.getTarget().getClass().getSimpleName()
                    + "."
                    + joinPoint.getSignature().getName(),
                duration),
        HTTP_INCOMING_REQUEST_URL,
        request.getRequestURI(),
        HTTP_REQUEST_METHOD,
        request.getMethod(),
        HTTP_RESPONSE_STATUS_CODE,
        String.valueOf(statusCode),
        HTTP_RESPONSE_BODY_CONTENT,
        responseBody);
  }

  /**
   * Method to log error.
   *
   * @param request HTTP Request object.
   * @param e Throwable exception.
   */
  private void logError(HttpServletRequest request, Throwable e) {
    withMdc(
        () ->
            log.error(
                "Exception: Method: {}, URI: {} failed with exception message: {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage()),
        HTTP_INCOMING_REQUEST_URL,
        request.getRequestURI(),
        HTTP_REQUEST_METHOD,
        request.getMethod());
  }

  /**
   * Utility method to execute logging with MDC context.
   *
   * @param runnable Runnable to execute.
   * @param keyValuePairs MDC key-value pairs.
   */
  private void withMdc(Runnable runnable, String... keyValuePairs) {
    for (int i = 0; i < keyValuePairs.length; i += 2) {
      MDC.put(keyValuePairs[i], (i + 1 < keyValuePairs.length) ? keyValuePairs[i + 1] : null);
      if (i + 1 >= keyValuePairs.length) {
        log.warn(
            "Odd number of keyValuePairs provided to withMdc. Key [{}] has no corresponding value.",
            keyValuePairs[i]);
      }
    }
    try {
      runnable.run();
    } finally {
      for (int i = 0; i < keyValuePairs.length; i += 2) {
        MDC.remove(keyValuePairs[i]);
      }
    }
  }

  /**
   * Method to extract the response status code.
   *
   * @param result Object post processing JoinPoint.
   * @return Response status code.
   */
  private int getStatusCode(Object result) {
    if (result instanceof ResponseEntity<?>) {
      return ((ResponseEntity<?>) result).getStatusCode().value();
    }
    return 0; // Default status code if no response object is available.
  }

  /**
   * Method to get HTTP request.
   *
   * @return the object of HttpServletRequest.
   */
  private HttpServletRequest getCurrentHttpRequest() {
    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
        .map(ServletRequestAttributes.class::cast)
        .map(ServletRequestAttributes::getRequest)
        .orElseThrow(() -> new IllegalStateException("No current HTTP request available"));
  }

  /**
   * Retrieve Request body from servlet request.
   *
   * @param joinPoint Proceeding Join point object.
   * @return Request body from servlet request.
   */
  private String getRequestBody(JoinPoint joinPoint) {
    return Arrays.stream(joinPoint.getArgs())
        .map(this::convertObjectToJson)
        .reduce((arg1, arg2) -> arg1 + ", " + arg2)
        .orElse("");
  }

  /**
   * Method to retrieve request headers as a formatted string.
   *
   * @param request HttpServletRequest object.
   * @return Request headers as a formatted string.
   */
  private String getRequestHeadersAsJson(HttpServletRequest request) {
    ObjectMapper headers = new ObjectMapper();
    Map<String, String> headersMap =
        Collections.list(request.getHeaderNames()).stream()
            .collect(Collectors.toMap(headerName -> headerName, request::getHeader));
    try {
      return headers.writeValueAsString(headersMap);
    } catch (JsonProcessingException e) {
      log.error("Error serializing headers to JSON", e);
      return "{}";
    }
  }

  /**
   * Method to convert Object to JSON format.
   *
   * @param object Object to be converted into JSON format.
   * @return JSON format of given object.
   */
  private String convertObjectToJson(Object object) {
    if (object == null) {
      return "";
    }
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Error serializing object to JSON", e);
      return "Error serializing object to JSON";
    }
  }
}
