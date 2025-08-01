package com.example.pushnotification.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI documentation. This class sets up the OpenAPI documentation for
 * the Push Notification APIs. It defines the API title, version, and description.
 *
 * @author Ramji Yadav
 */
@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Push Notification Operations",
            version = "v1",
            description = "API documentation for Push Notification operations."),
    servers = {@Server(url = "${gateway.hostname}", description = "API Gateway URL")})
@SecurityScheme(
    name = "bearerAuth",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {}
