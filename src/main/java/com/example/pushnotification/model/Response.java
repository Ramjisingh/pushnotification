package com.example.pushnotification.model;

import lombok.Data;


@Data
public class Response<T> {

    private boolean success;
    private String message;
    private T data;


    public Response(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }


}
