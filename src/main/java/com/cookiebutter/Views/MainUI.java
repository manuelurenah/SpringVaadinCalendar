package com.cookiebutter.Views;

import com.cookiebutter.Models.CustomEvent;
import com.cookiebutter.Models.CustomEventProvider;
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
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import org.springframework.beans.factory.annotation.Autowired;

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
    private EventService eventService;
    @Autowired
    private UserService userService;

    File baseDir = VaadinService.getCurrent().getBaseDirectory();
    Button configBtn = new Button(new FileResource(new File(baseDir.getAbsolutePath() + "/icons/Settings-24.png")));
    Button addBtn = new Button("Add New Event");
    Calendar calendar = new Calendar();

    @Override
    protected void init(VaadinRequest request) {
        Page.getCurrent().setTitle("Spring Vaadin Calendar");

        setUpCalendar();
        setUpButtonModalView(addBtn, "Add New Event", new EventForm());
        setUpButtonModalView(configBtn, "Update User Info", new UserForm());

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setSizeFull();

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeUndefined();
        buttons.setSpacing(true);

        buttons.addComponents(addBtn, configBtn);

        layout.addComponent(buttons);
        layout.addComponent(calendar);
        layout.setComponentAlignment(buttons, Alignment.TOP_RIGHT);
        layout.setExpandRatio(calendar, 1);

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
        calendar.setFirstVisibleDayOfWeek(1);
        calendar.setLastVisibleDayOfWeek(7);
        calendar.setFirstVisibleHourOfDay(6);
        calendar.setLastVisibleHourOfDay(20);
        calendar.setSizeFull();
    }

    private void setUpButtonModalView(Button button, String title, FormLayout form) {
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window modalView = new Window(title);
                modalView.center();
                modalView.setResizable(false);
                modalView.setModal(true);
                modalView.setClosable(true);
                modalView.setDraggable(false);
                modalView.setContent(form);

                addWindow(modalView);
            }
        });
    }

    private boolean isThisMonth(java.util.Calendar cal, Date date) {
        cal.setTime(new Date());
        int thisMonth = cal.get(java.util.Calendar.MONTH);
        cal.setTime(date);
        return cal.get(java.util.Calendar.MONTH) == thisMonth;
    }

}
