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
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomEventProvider customEventProvider;
    @Autowired
    private EventForm eventForm;
    @Autowired
    private LoginUI login;

    File baseDir = VaadinService.getCurrent().getBaseDirectory();
    Button configBtn = new Button(new FileResource(new File(baseDir.getAbsolutePath() + "/icons/Settings-24.png")));
    Button addBtn = new Button("Add New Event");
    Calendar calendar = new Calendar();

    @Override
    protected void init(VaadinRequest request) {

        User currentUser = (User) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("current_user");

        System.out.print("Got the user: ");
        System.out.println(currentUser);

        Page.getCurrent().setTitle("Spring Vaadin Calendar");
        eventForm.setCalendar(calendar);

        setUpCalendar();
        setUpButtonModalView(addBtn, "Add New Event", eventForm);
        setUpButtonModalView(configBtn, "Update User Info", new UserForm());

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setSizeFull();

        HorizontalLayout buttons = new HorizontalLayout();
        HorizontalLayout calendarButtons = new HorizontalLayout();
        HorizontalLayout optionButtons = new HorizontalLayout();
        buttons.setSizeUndefined();
        buttons.setSpacing(true);

        // Calendar buttons:
        Button monthCalendarButton = new Button("Monthly Calendar");
        monthCalendarButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // set month to calendar stuff:
                calendar.setStartDate(new GregorianCalendar().getTime());
                GregorianCalendar calEnd = new GregorianCalendar();
                calEnd.set(java.util.Calendar.DATE, 1);
                calEnd.roll(java.util.Calendar.DATE, -1);
                calendar.setEndDate(calEnd.getTime());
            }
        });

        Button weekCalendarButton = new Button("Weekly Calendar");
        weekCalendarButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                GregorianCalendar weekstart = new GregorianCalendar();
                GregorianCalendar weekend   = new GregorianCalendar();
                weekstart.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
                weekstart.set(java.util.Calendar.HOUR_OF_DAY, 0);
                weekstart.set(java.util.Calendar.DAY_OF_WEEK,
                        java.util.Calendar.SUNDAY);
                weekend.set(java.util.Calendar.HOUR_OF_DAY, 23);
                weekend.set(java.util.Calendar.DAY_OF_WEEK,
                        java.util.Calendar.SATURDAY);
                calendar.setStartDate(weekstart.getTime());
                calendar.setEndDate(weekend.getTime());
            }
        });

        Button dayCalendarButton = new Button("Daily View");
        dayCalendarButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                GregorianCalendar today = new GregorianCalendar();
                calendar.setStartDate(today.getTime());
                calendar.setEndDate(today.getTime());
            }
        });

        calendarButtons.addComponents(monthCalendarButton, weekCalendarButton, dayCalendarButton );


        optionButtons.addComponents(addBtn, configBtn);
        buttons.addComponents(calendarButtons, optionButtons);

        layout.addComponent(buttons);
        layout.addComponent(calendar);
        layout.setComponentAlignment(buttons, Alignment.TOP_RIGHT);
        layout.setExpandRatio(calendar, 1);

        if (currentUser != null) {
            setContent(layout);
        } else {
            setContent(login);
        }
    }

    private void setUpCalendar() {
        //Setup event provider.
        calendar.setEventProvider(customEventProvider);

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
                CustomEvent e = (CustomEvent) event;
                e.setStart(start);
                e.setEnd(end);
                eventService.save(e);
            }
        });

        calendar.setHandler(new BasicEventResizeHandler() {
            private static final long twelveHoursInMs = 12*60*60*1000;

            protected void setDates(EditableCalendarEvent event,
                                    Date start, Date end) {
                CustomEvent e = (CustomEvent) event;
                e.setStart(start);
                e.setEnd(end);
                eventService.save(e);
            }
        });
        calendar.setHandler(new CalendarComponentEvents.RangeSelectHandler() {
            @Override
            public void rangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
                eventForm.setDates(event.getStart(), event.getEnd());
                openModalView("Add New Event", eventForm);
            }
        });

        calendar.setLocale(Locale.US);
        calendar.setStartDate(new GregorianCalendar().getTime());
        GregorianCalendar calEnd = new GregorianCalendar();
        calEnd.set(java.util.Calendar.DATE, 1);
        calEnd.roll(java.util.Calendar.DATE, -1);
        calendar.setEndDate(calEnd.getTime());
        calendar.setTimeFormat(TimeFormat.Format12H);
        calendar.setFirstVisibleDayOfWeek(1);
        calendar.setLastVisibleDayOfWeek(7);
        calendar.setFirstVisibleHourOfDay(6);
        calendar.setLastVisibleHourOfDay(20);
        calendar.setSizeFull();
    }
    private void openModalView(String title, FormLayout form) {
        Window modalView = new Window(title);
        modalView.center();
        modalView.setResizable(false);
        modalView.setModal(true);
        modalView.setClosable(true);
        modalView.setDraggable(false);
        modalView.setContent(form);

        addWindow(modalView);
    }
    private void setUpButtonModalView(Button button, String title, FormLayout form) {
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                openModalView(title, form);
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
