package com.cookiebutter.Components;

import ch.qos.logback.core.net.server.Client;
import com.cookiebutter.Services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.sparkpost.exception.SparkPostException;

/**
 * Created by luis on 20/10/16.
 */
@Component
public class Scheduler {
    @Autowired
    private EventService eventService;

    @Scheduled(fixedRate = 10000)
    public void logSomething() throws SparkPostException{
        String API_KEY = "4243effa31c039d6175cc6c511f3b8c991641172";
        com.sparkpost.Client client = new com.sparkpost.Client(API_KEY);

        client.sendMessage(
                "lrojas94@sparkpostbox.com",
                "lrojas94@gmail.com",
                "The subject of the message",
                "The text part of the email",
                "<b>The HTML part of the email</b>");
    }
}
