package com.cookiebutter.Models;

import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent.EventChangeNotifier;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "event")
public class CustomEvent implements CalendarEvent, EditableCalendarEvent, EventChangeNotifier {

    @Id
    @GeneratedValue
    private long id;
    @Column
    @NotNull
    private String caption;
    @Column
    private String description;
    @Column
    private String styleName;
    @Column
    @NotNull
    private boolean isAllDay;
    @Column
    private Date start;
    @Column
    private Date end;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    private transient List<EventChangeListener> listeners = new ArrayList<EventChangeListener>();

    public CustomEvent() { }

    public CustomEvent(String caption, String description, boolean isAllDay, Date start, Date end) {
        this.caption = caption;
        this.description = description;
        this.isAllDay = isAllDay;
        this.start = start;
        this.end = end;
    }

    @Override
    public void addEventChangeListener(EventChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeEventChangeListener(EventChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        fireEventChange();
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        fireEventChange();
    }

    @Override
    public void setEnd(Date end) {
        this.end = end;
        fireEventChange();
    }

    @Override
    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    @Override
    public void setAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
        fireEventChange();
    }

    @Override
    public Date getStart() {
        return start;
    }

    @Override
    public Date getEnd() {
        return end;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getStyleName() {
        return styleName;
    }

    @Override
    public boolean isAllDay() {
        return isAllDay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    protected void fireEventChange() {
        EventChangeEvent event = new EventChangeEvent(this);

        for (EventChangeListener listener : listeners) {
            listener.eventChange(event);
        }
    }
}
