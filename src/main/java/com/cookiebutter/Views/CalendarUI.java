package com.cookiebutter.Views;

import com.cookiebutter.Models.CustomEvent;
import com.cookiebutter.Models.CustomEventProvider;
import com.cookiebutter.Services.EventService;
import com.cookiebutter.Services.UserService;
import com.cookiebutter.Views.event.EventForm;
import com.cookiebutter.Views.user.UserForm;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by MEUrena on 10/24/16.
 * All rights reserved.
 */
@Component
@UIScope
@SpringUI
public class CalendarUI extends VerticalLayout {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    private EventForm eventForm;
    private UserForm userForm;
    private CustomEventProvider customEventProvider;
    private MainUI mainUI;

    File baseDir = VaadinService.getCurrent().getBaseDirectory();
    Button configBtn = new Button(new FileResource(new File(baseDir.getAbsolutePath() + "/icons/Settings-24.png")));
    Button logoutButton = new Button(new FileResource(new File(baseDir.getAbsolutePath() + "/icons/Exit-24.png")));
    Button addBtn = new Button("Add New Event");
    Calendar calendar = new Calendar();

    @Autowired
    public CalendarUI(EventForm eventForm, UserForm userForm, CustomEventProvider customEventProvider) {
        this.eventForm = eventForm;
        this.userForm = userForm;
        this.customEventProvider = customEventProvider;

        this.eventForm.setCalendar(calendar);

        setUpCalendar();
        setUpButtonModalView(addBtn, "Add New Event", eventForm);
        setUpButtonModalView(configBtn, "Update User Info", userForm);

        setSpacing(true);
        setMargin(true);
        setSizeFull();

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
                GregorianCalendar weekend = new GregorianCalendar();
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

        logoutButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                VaadinService.getCurrentRequest().getWrappedSession().removeAttribute("current_user");
                getUI().getPage().setLocation("/");
            }
        });

        calendarButtons.addComponents(monthCalendarButton, weekCalendarButton, dayCalendarButton);

        optionButtons.addComponents(addBtn, configBtn, logoutButton);
        buttons.addComponents(calendarButtons, optionButtons);

        addComponent(buttons);
        addComponent(calendar);
        setComponentAlignment(buttons, Alignment.TOP_RIGHT);
        setExpandRatio(calendar, 1);
    }

    public void setupMainUI(MainUI mainUI) {
        this.mainUI = mainUI;
    }

    private void setUpCalendar() {
        //Setup event provider.
        calendar.setEventProvider(customEventProvider);

        calendar.setHandler(new CalendarComponentEvents.EventClickHandler() {
            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) {
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
            private static final long twelveHoursInMs = 12 * 60 * 60 * 1000;

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
        calendar.setTimeFormat(Calendar.TimeFormat.Format12H);
        calendar.setFirstVisibleDayOfWeek(1);
        calendar.setLastVisibleDayOfWeek(7);
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

        mainUI.addWindow(modalView);
    }

    private void setUpButtonModalView(Button button, String title, FormLayout form) {
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                userForm.setUser(userService.getCurrentUser());
                openModalView(title, form);
            }
        });
    }

}
