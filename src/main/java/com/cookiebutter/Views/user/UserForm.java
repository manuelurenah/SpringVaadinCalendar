package com.cookiebutter.Views.user;

import com.cookiebutter.Models.CustomEventProvider;
import com.cookiebutter.Models.User;
import com.cookiebutter.Services.UserService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by MEUrena on 10/18/16.
 * All rights reserved.
 */

@Component
@UIScope
@SpringUI
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
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        update.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        update.addClickListener((Button.ClickListener) event -> {
            userService.save(user);
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("current_user", user);
            ((Window)getParent()).close();
        });

        cancel.addClickListener((Button.ClickListener) event -> {
            ((Window)getParent()).close();
        });

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponent(update);
        buttons.addComponent(cancel);

        name.setCaption("First Name:");
        lastname.setCaption("Last Name:");
        email.setCaption("E-Mail:");

        addComponents(name, lastname, email, buttons);
    }

    public void setUser(User user) {
        this.user = user;
        BeanFieldGroup.bindFieldsUnbuffered(user, this);
    }

}
