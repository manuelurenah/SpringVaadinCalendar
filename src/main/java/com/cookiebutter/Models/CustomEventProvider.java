package com.cookiebutter.Models;

import com.cookiebutter.Services.EventService;
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
public class CustomEventProvider implements CalendarEventProvider {

    @Autowired
    EventService eventService;

    List<CustomEvent> events = new ArrayList<>();

    public CustomEventProvider() {
        events = new ArrayList<>();
    }

    public void addEvent(CustomEvent event) {
        events.add(event);
    }

    @Override
    public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
        return null;
    }
}
