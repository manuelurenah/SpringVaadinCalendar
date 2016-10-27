package com.cookiebutter.Components;

import ch.qos.logback.core.net.server.Client;
import com.cookiebutter.Models.CustomEvent;
import com.cookiebutter.Models.User;
import com.cookiebutter.Services.EventService;
import com.cookiebutter.Services.UserService;
import com.vaadin.server.VaadinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.sparkpost.exception.SparkPostException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by luis on 20/10/16.
 */
@Component
public class Scheduler {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 10000)
    public void logSomething() throws SparkPostException{
        String API_KEY = "6755d9d3e2e56120adfa2a69af70479c4843d7eb";
        com.sparkpost.Client client = new com.sparkpost.Client(API_KEY);

        Date begin = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(begin);
        cal.add(Calendar.HOUR, 1);
        Date end = cal.getTime();
        List<CustomEvent> pendingEvents = eventService.findBetween(begin, end);

        User current = userService.findOne();

        if (pendingEvents != null && pendingEvents.size() > 0) {
            String senTo = current.getEmail();
            for(CustomEvent event : pendingEvents) {
                if (!event.isNotified()) {
                    event.setNotified(true);
                    eventService.save(event);
                    client.sendMessage(
                            "manuel-ureh@sparkpostbox.com",
                            senTo,
                            "New Events Coming Up",
                            event.getCaption() + "\n\n" + event.getDescription(),
                            "<b>Auto sent via Spring Vaadin Calendar</b>");
                }
            }
        }
    }
}
