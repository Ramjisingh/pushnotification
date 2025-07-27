package com.example.pushnotification;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.pushnotification"})
@Log4j2
public class PushNotificationApplication {

  public static void main(String[] args) {
    SpringApplication.run(PushNotificationApplication.class, args);


  }

}