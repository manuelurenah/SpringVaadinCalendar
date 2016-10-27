package com.cookiebutter.Repositories;

import com.cookiebutter.Models.CustomEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by MEUrena on 10/17/16.
 * All rights reserved.
 */
public interface EventRepository extends CrudRepository<CustomEvent, Long> {
    List<CustomEvent> findAllByStartAndEnd(Date start, Date end);
    @Query("select e from CustomEvent e " +
            "where e.start between ?1 and ?2 and e.notified = false")
    List<CustomEvent> findByDatesBetween(Date startDate, Date endDate);
}
