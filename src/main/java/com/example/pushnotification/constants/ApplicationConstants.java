package com.example.pushnotification.constants;

public class ApplicationConstants {

    // Json response constants
    public static final String INTERNAL_CODE = "4001";
    public static final String REASON = "Missing Fields";
    public static final String FIELD = "field";
    public static final String AUTHORISATION = "Authorization";
    public static final String DESCRIPTION = "Please complete the following fields";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String DEVICE_ID = "deviceid";
    public static final String DEVICE_ID_MISSING_IN_HEADER =
            "Required request header 'deviceId' for method parameter type String is not present";
    public static final String TRACE_ID = "traceId";

    private ApplicationConstants() {
        throw new IllegalStateException(
                "ApplicationConstants class which cannot be instantiated directly!!");
    }

}
