package com.cookiebutter.Components;

import com.cookiebutter.Models.User;
import com.cookiebutter.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by MEUrena on 10/23/16.
 * All rights reserved.
 */
@Component
public class InitialSetup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    UserService userService;

    boolean alreadySetup = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        if(!userService.exists("admin@me.com")) {
            User admin = new User();
            admin.setName("Admin");
            admin.setLastname("Admin");
            admin.setEmail("admin@me.com");
            admin.setPassword("admin");

            userService.save(admin);
        }

        alreadySetup = true;
    }
}
