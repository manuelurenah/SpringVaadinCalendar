package com.cookiebutter.Services;

import com.cookiebutter.Models.CustomEvent;
import com.cookiebutter.Repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by MEUrena on 10/18/16.
 * All rights reserved.
 */

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<CustomEvent> findAll() {
        return (List<CustomEvent>) eventRepository.findAll();
    }

    public List<CustomEvent> findAllByStartAndEndDates(Date start, Date end) {
        return eventRepository.findAllByStartAndEnd(start, end);
    }

    @Transactional
    public CustomEvent save(CustomEvent customEvent) {
        eventRepository.save(customEvent);
        return customEvent;
    }

    @Transactional
    public boolean delete(CustomEvent customEvent) {
        eventRepository.delete(customEvent);
        return true;
    }

}
