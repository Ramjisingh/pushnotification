package com.example.pushnotification.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.example.pushnotification.model.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class FCMService {

    private Logger logger = LoggerFactory.getLogger(FCMService.class);

    private static final String DEVICE_ID_KEY = "DeviceId";
    private static final String CONSIGNMENT_KEY = "Consignment";
    private static final String TITLE_KEY = "Title";
    private static final String MESSAGE_KEY = "Message";
    private static final String SCREENID = "ScreenId";


    public void sendMessageToToken(PushNotificationRequest request) throws InterruptedException, ExecutionException {


        //============================================================================
        Map<String, String> data = buildNotificationData(request);
        System.out.println("data=====" + data.toString());

        // Get the preconfigured message with custom data
        Message message = getPreconfiguredMessageWithData(data, request);

        //------------------------------------------------------------------------------------

        //Message message = getPreconfiguredMessageToToken(request);
        System.out.println("message=====" + message.toString());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);

        System.out.println("jsonOutput==========: " + jsonOutput);

        String response = sendAndGetResponse(message);
        System.out.println("Response from FCM is: " + response);

        logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response + " msg " + jsonOutput);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }


    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setChannelId("")
                        .setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).
                setToken(request.getToken())
                .build();
    }

    private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                .setTopic(request.getTopic())
                .build();
    }

    private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                .putAllData(data)
                .setToken(request.getToken())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
                        new Notification(request.getTitle(), request.getMessage()));
    }

    private Map<String, String> buildNotificationData(PushNotificationRequest request) {

        Map<String, String> data = new HashMap<>();
        data.put(TITLE_KEY, request.getTitle());
        data.put(MESSAGE_KEY, request.getMessage());
        data.put(DEVICE_ID_KEY, "IT~CT1~~MC67NA");
        data.put(CONSIGNMENT_KEY, "880967282769");
        data.put(SCREENID, "fedex://addconsignment/");
        return data;

    }
}