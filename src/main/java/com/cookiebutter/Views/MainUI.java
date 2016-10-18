package com.cookiebutter.Views;

import com.cookiebutter.Services.EventService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by MEUrena on 10/18/16.
 * All rights reserved.
 */

@SpringUI(path = "/")
@Theme("valo")
public class MainUI extends UI {

    @Autowired
    private EventService eventService;

    Calendar calendar = new Calendar("AdminCalendar");

    @Override
    protected void init(VaadinRequest request) {
        calendar.setSizeFull();
        VerticalLayout layout = new VerticalLayout(calendar);
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
    }
}
