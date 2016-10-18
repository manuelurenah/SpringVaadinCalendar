package com.cookiebutter.Views.user;

import com.cookiebutter.Models.User;
import com.cookiebutter.Services.UserService;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by MEUrena on 10/18/16.
 * All rights reserved.
 */

@Component
@UIScope
public class UserForm extends FormLayout {

    @Autowired
    private UserService userService;
    private User user;

    TextField name = new TextField("Name");
    TextField lastname = new TextField("LastName");
    TextField email = new TextField("E-mail");
    TextField password = new TextField();
    TextField confirm = new TextField();

    Button update = new Button("Update");
    Button cancel = new Button("Cancel");

    public UserForm() {
        HorizontalLayout buttons = new HorizontalLayout();
    }

}
