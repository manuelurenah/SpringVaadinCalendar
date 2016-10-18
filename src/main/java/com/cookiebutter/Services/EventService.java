package com.cookiebutter.Services;

import com.cookiebutter.Models.Event;
import com.cookiebutter.Models.User;
import com.cookiebutter.Repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by MEUrena on 10/18/16.
 * All rights reserved.
 */

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public Event save(Event event) {
        eventRepository.save(event);
        return event;
    }

    @Transactional
    public boolean delete(Event event) {
        eventRepository.delete(event);
        return true;
    }

}
