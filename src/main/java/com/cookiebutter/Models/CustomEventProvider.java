package com.cookiebutter.Models;

import com.cookiebutter.Services.EventService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.BasicEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MEUrena on 10/18/16.
 * All rights reserved.
 */
@SpringUI
public class CustomEventProvider extends BasicEventProvider {

    @Autowired
    EventService eventService;

    List<CalendarEvent> events;

    public CustomEventProvider() {
        events = new ArrayList<>();
    }

    @Override
    public void addEvent(CalendarEvent event) {
        super.addEvent(event);
        CustomEvent e = (CustomEvent) event;
        eventService.save(e);
    }

    @Override
    public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
        events = new ArrayList<>(eventService.findBetween(startDate, endDate));
        return events;
    }
}
