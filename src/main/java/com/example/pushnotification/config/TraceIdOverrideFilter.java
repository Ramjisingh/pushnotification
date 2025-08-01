package com.example.pushnotification.config;

import static com.example.pushnotification.constants.ApplicationConstants.TRACE_ID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This class is used to override the traceId from the request header to the MDC (Mapped Diagnostic
 * Context) for logging purposes.
 */
@Component
public class TraceIdOverrideFilter extends OncePerRequestFilter {

  /**
   * This method is used to override the traceId from the request header to the MDC (Mapped
   * Diagnostic Context)
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String traceIdFromRequest = request.getHeader(TRACE_ID);
    if (traceIdFromRequest != null) {
      MDC.put(TRACE_ID, traceIdFromRequest);
    }
    filterChain.doFilter(request, response);
    MDC.remove(TRACE_ID);
  }
}
