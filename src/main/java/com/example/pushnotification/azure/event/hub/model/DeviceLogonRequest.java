package com.example.pushnotification.azure.event.hub.model;

import com.example.pushnotification.util.ValidTimeFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Device Logon Request entity")
@Setter
@Getter
@AllArgsConstructor
public class DeviceLogonRequest {

    @Schema(hidden = true)
    private String deviceId;

    @Schema(description = "MessageNumber for device logon ", example = "00009191")
    @Size(max = 8, message = "messageNumber must not exceed 8 characters")
    private String messageNumber;

    @Schema(description = "Request Code Identifier", example = "LN")
    @NotNull(message = "requestCodeIdentifier must not be null.")
    @Pattern(regexp = "LN", message = "requestCodeIdentifier must be 'LN'")
    private String requestCodeIdentifier;

    @Schema(description = "Round Id for logon request", example = "SUD41")
    @NotBlank(message = "roundId must not be blank or null")
    @Size(min = 1, max = 5, message = "roundId length must be between 1-5 characters long")
    private String roundId;

    @Schema(description = "UserGLId for logon request", example = "U682YLE")
    @NotBlank(message = "User GL Id must not be blank or null")
    @Size(min = 1, max = 8, message = "userGLId must contain between 1 to 8 characters")
    private String userGLId;

    @Schema(description = "OpenMileage for logon request", example = "0000000")
    @Size(max = 7, message = "OpenMileage must not exceed 7 characters")
    private String openMileage;

    @Schema(description = "Time for logon request with format HHMM", example = "1900")
    @NotBlank(message = "time must not be blank or null")
    @Size(min = 4, max = 4, message = "time must be exactly 4 digits")
    @ValidTimeFormat(message = "time must be in valid format of HHMM")
    private String time;
}