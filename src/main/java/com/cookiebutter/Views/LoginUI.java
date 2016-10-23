package com.cookiebutter.Views;

import com.cookiebutter.Models.User;
import com.cookiebutter.Services.UserService;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by MEUrena on 10/21/16.
 * All rights reserved.
 */

@Component
@UIScope
@SpringUI
public class LoginUI extends FormLayout {

    @Autowired
    private UserService userService;
    private User user;

    TextField usernameField = new TextField("E-mail");
    PasswordField passwordField = new PasswordField("Password");

    Button loginButton = new Button("Login");

    public LoginUI() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String email = usernameField.getValue();
                System.out.println("Email: " + email);
                String password = passwordField.getValue();
                System.out.println("Pass: " + password);
                user = userService.findByEmail(email);
                if (user != null) {
                    if (user.getPassword().equals(password)) {
                        saveUserInSession(LoginUI.this, user);
                    } else {
                        showErrorMessage("The password is incorrect.");
                    }
                } else {
                    showErrorMessage("The user requested doesn't exists.");
                }
            }
        });

        usernameField.setRequired(true);
        usernameField.setCaption("E-mail:");
        usernameField.addValidator(new EmailValidator("Username must be an E-mail address."));
        passwordField.setRequired(true);
        passwordField.setCaption("Password:");

        addComponents(usernameField, passwordField, loginButton);

    }

    private void saveUserInSession(LoginUI ui, User user) {
        ui.user = user;

        ui.getSession().setAttribute("current_user", user);
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("current_user", user);
        getUI().getPage().setLocation("/");
    }

    private void showErrorMessage(String message) {
        Notification.show(message, Notification.Type.ERROR_MESSAGE);
    }

}
