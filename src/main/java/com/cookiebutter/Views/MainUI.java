package com.cookiebutter.Views;

import com.cookiebutter.Models.CustomEvent;
import com.cookiebutter.Models.CustomEventProvider;
import com.cookiebutter.Models.User;
import com.cookiebutter.Services.EventService;
import com.cookiebutter.Services.UserService;
import com.cookiebutter.Views.event.EventForm;
import com.cookiebutter.Views.user.UserForm;
import com.vaadin.annotations.Theme;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.Calendar.TimeFormat;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by MEUrena on 10/18/16.
 * All rights reserved.
 */

@SpringUI(path = "/")
@Theme("valo")
public class MainUI extends UI {

    @Autowired
    private EventForm eventForm;
    @Autowired
    private UserForm userForm;
    @Autowired
    private CustomEventProvider customEventProvider;
    @Autowired
    private CalendarUI calendarUI;
    @Autowired
    private LoginUI login;

    @Override
    protected void init(VaadinRequest request) {

        User currentUser = (User) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("current_user");

        Page.getCurrent().setTitle("Spring Vaadin Calendar");
        calendarUI.setupMainUI(MainUI.this);

        if (currentUser != null) {
            setContent(calendarUI);
        } else {
            setContent(login);
        }
    }

}
