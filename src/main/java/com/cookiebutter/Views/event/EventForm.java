package com.cookiebutter.Views.event;

import com.cookiebutter.Services.EventService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by MEUrena on 10/19/16.
 * All rights reserved.
 */

@Component
@UIScope
public class EventForm extends FormLayout {

    @Autowired
    EventService eventService;

    DateField start = new DateField("Start Date");
    DateField end = new DateField("End Date");
    CheckBox allday = new CheckBox("All Day", false);

    TextField caption = new TextField("Caption");
    TextArea description = new TextArea("Description");

    Button addBtn = new Button("Add");
    Button cancelBtn = new Button("Cancel");

    public EventForm() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        addBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        addBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Should add new event with task", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        cancelBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Should close modal", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        allday.addValueChangeListener(e -> Notification.show("Value changed:",
                String.valueOf(e.getProperty().getValue()),
                Notification.Type.TRAY_NOTIFICATION));

        HorizontalLayout buttons = new HorizontalLayout(addBtn, cancelBtn);
        buttons.setSpacing(true);

        start.setCaption("Start Date:");
        end.setCaption("Start Date:");
        caption.setCaption("Caption:");
        description.setCaption("Description:");

        addComponents(start, end, allday, caption, description, buttons);

    }

}
