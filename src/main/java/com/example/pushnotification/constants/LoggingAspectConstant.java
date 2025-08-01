package com.example.pushnotification.constants;

/**
 * Constant file for Logging Aspect
 *
 * @author Ramji
 */
public final class LoggingAspectConstant {
  // Json log constants
  public static final String HTTP_INCOMING_REQUEST_URL = "http.incoming.request.url";
  public static final String HTTP_REQUEST_METHOD = "http.request.method";
  public static final String HTTP_REQUEST_BODY_CONTENT = "http.request.body.content";
  public static final String HTTP_RESPONSE_STATUS_CODE = "http.response.status_code";
  public static final String HTTP_RESPONSE_BODY_CONTENT = "http.response.body.content";
  public static final String HTTP_REQUEST_HEADERS = "http.request.headers.Form-Source";

  // @Around annotation constants
  public static final String GET_MAPPING_ANNOTATION =
      "@annotation(org.springframework.web.bind.annotation.GetMapping)";
  public static final String PUT_MAPPING_ANNOTATION =
      "@annotation(org.springframework.web.bind.annotation.PutMapping)";
  public static final String POST_MAPPING_ANNOTATION =
          "@annotation(org.springframework.web.bind.annotation.PostMapping)";
  public static final String DELETE_MAPPING_ANNOTATION =
      "@annotation(org.springframework.web.bind.annotation.DeleteMapping)";

  private LoggingAspectConstant() {
    throw new IllegalStateException(
        "LoggingAspectConstant class which cannot be instantiated directly!!");
  }
}
