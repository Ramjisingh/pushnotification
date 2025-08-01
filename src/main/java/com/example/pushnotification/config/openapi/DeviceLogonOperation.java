package com.example.pushnotification.config.openapi;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

import com.example.pushnotification.azure.event.hub.model.DeviceLogonRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.MediaType;

/**
 * This DeviceLogonOperation annotation is used to add the swagger configuration for the device
 * logon api. It provides sample request and different http status code.
 *
 * @author 6709789
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Logon to device",
        description = "Sends logon request to the host system.",
        parameters = {
                @Parameter(
                        name = "traceId",
                        in = HEADER,
                        description =
                                "Identifier used for correlating requests and responses across microservices, aiding in troubleshooting and performance monitoring.",
                        example = "68248e6724f15ba6ab1a561de30af2df",
                        schema = @Schema(type = "string")),
                @Parameter(
                        name = "deviceId",
                        in = HEADER,
                        description =
                                "Identifier used to distinguish devices making API calls, supporting event tracking and targeted data delivery.",
                        example = "MAN~~MC67NA~",
                        schema = @Schema(type = "string"),
                        required = true)
        },
        requestBody =
        @RequestBody(
                content =
                @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = DeviceLogonRequest.class))),
        responses = {
                @ApiResponse(
                        responseCode = "202",
                        description = "Device logon request accepted successfully"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid request",
                        content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema =
                                @Schema(
                                        example =
                                                """
                                                                  {
                                                                    "code": "400",
                                                                    "status": "Bad Request",
                                                                    "message": "Validation error",
                                                                    "details": [
                                                                      {
                                                                        "parameters": [
                                                                          {
                                                                            "key": "roundId",
                                                                            "value": "roundId length must be between 1-5 characters long"
                                                                          },
                                                                          {
                                                                            "key": "roundId",
                                                                            "value": "roundId must not be blank or null"
                                                                          }
                                                                        ]
                                                                      }
                                                                    ]
                                                                  }
                                                                  """))),
        })
public @interface DeviceLogonOperation {}