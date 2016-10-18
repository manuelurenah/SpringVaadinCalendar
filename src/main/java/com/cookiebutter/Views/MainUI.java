package com.cookiebutter.Views;

import com.cookiebutter.Models.CustomEvent;
import com.cookiebutter.Models.CustomEventProvider;
import com.cookiebutter.Services.EventService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Calendar.TimeFormat;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import org.springframework.beans.factory.annotation.Autowired;

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
    private EventService eventService;

    Calendar calendar = new Calendar("Admin Calendar");

    @Override
    protected void init(VaadinRequest request) {
        setUpCalendar();

        CustomEvent newEvent = new CustomEvent("The Event", "Single Event", false,
                new GregorianCalendar(2016, 10, 18, 12, 00).getTime(),
                new GregorianCalendar(2016, 10, 18, 14, 00).getTime());

        calendar.addEvent(newEvent);

        VerticalLayout layout = new VerticalLayout(calendar);
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    private void setUpCalendar() {
        calendar.setHandler(new EventClickHandler() {
            @Override
            public void eventClick(EventClick event) {
                CustomEvent e = (CustomEvent) event.getCalendarEvent();

                new Notification("Event clicked: " + e.getCaption(), e.getDescription()).show(Page.getCurrent());
            }
        });

        calendar.setHandler(new BasicEventMoveHandler() {
            private java.util.Calendar javaCalendar;

            public void eventMove(CalendarComponentEvents.MoveEvent event) {
                javaCalendar = event.getComponent().getInternalCalendar();
                super.eventMove(event);
            }

            protected void setDates(EditableCalendarEvent event,
                                    Date start, Date end) {
                if (isThisMonth(javaCalendar, start)
                        && isThisMonth(javaCalendar, end)) {
                    super.setDates(event, start, end);
                }
            }
        });

        calendar.setHandler(new BasicEventResizeHandler() {
            private static final long twelveHoursInMs = 12*60*60*1000;

            protected void setDates(EditableCalendarEvent event,
                                    Date start, Date end) {
                long eventLength = end.getTime() - start.getTime();
                if (eventLength <= twelveHoursInMs) {
                    super.setDates(event, start, end);
                }
            }
        });

        calendar.setLocale(Locale.US);
        calendar.setTimeFormat(TimeFormat.Format12H);
        calendar.setSizeFull();
    }

    private boolean isThisMonth(java.util.Calendar cal, Date date) {
        cal.setTime(new Date());
        int thisMonth = cal.get(java.util.Calendar.MONTH);
        cal.setTime(date);
        return cal.get(java.util.Calendar.MONTH) == thisMonth;
    }

}
