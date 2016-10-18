package com.cookiebutter.Repositories;

import com.cookiebutter.Models.CustomEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by MEUrena on 10/17/16.
 * All rights reserved.
 */
public interface EventRepository extends CrudRepository<CustomEvent, Long> {
    List<CustomEvent> findAllByStartAndEnd(Date start, Date end);
}
