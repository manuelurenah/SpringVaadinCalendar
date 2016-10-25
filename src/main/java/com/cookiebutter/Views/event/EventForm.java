package com.cookiebutter.Views.event;

import com.cookiebutter.Models.CustomEvent;
import com.cookiebutter.Models.User;
import com.cookiebutter.Services.EventService;
import com.cookiebutter.Services.UserService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by MEUrena on 10/19/16.
 * All rights reserved.
 */

@Component
@UIScope
public class EventForm extends FormLayout {

    @Autowired
    EventService eventService;

    Calendar calendar;

    DateField start = new PopupDateField("Start Date");
    DateField end = new PopupDateField("End Date");

    TextField caption = new TextField("Caption");
    TextArea description = new TextArea("Description");

    Button addBtn = new Button("Add");
    Button cancelBtn = new Button("Cancel");

    public EventForm(Date startDate, Date endDate) {
        start.setValue(startDate);
        end.setValue(endDate);
        setup();
    }

    public EventForm() {
        start.setValue(new Date());
        end.setValue(new Date());
        setup();

    }

    private void setup() {

        setSizeUndefined();
        setMargin(true);
        setSpacing(true);
        start.setResolution(Resolution.MINUTE);
        end.setResolution(Resolution.MINUTE);
        addBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        addBtn.addClickListener((Button.ClickListener) event -> {
            CustomEvent e = new CustomEvent();
            e.setDescription(description.getValue());
            e.setCaption(caption.getValue());
            e.setStart(start.getValue());
            e.setEnd(end.getValue());
            e.setAllDay(false);
            calendar.addEvent(e);
            ((Window)getParent()).close();
        });

        cancelBtn.addClickListener((Button.ClickListener) event -> {
            ((Window)getParent()).close();
        });

        HorizontalLayout buttons = new HorizontalLayout(addBtn, cancelBtn);
        buttons.setSpacing(true);

        start.setCaption("Start Date:");
        end.setCaption("Start Date:");
        caption.setCaption("Caption:");
        description.setCaption("Description:");

        addComponents(start, end, caption, description, buttons);
    }

    public void setDates(Date startDate, Date endDate) {
        start.setValue(startDate);
        end.setValue(endDate);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
