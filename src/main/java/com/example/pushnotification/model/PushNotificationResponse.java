package com.example.pushnotification.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PushNotificationResponse {

    private int status;
    private String message;
    public PushNotificationResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public PushNotificationResponse() {
    }

}