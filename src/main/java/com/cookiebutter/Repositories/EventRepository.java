package com.cookiebutter.Repositories;

import com.cookiebutter.Models.Event;
import com.cookiebutter.Models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by MEUrena on 10/17/16.
 * All rights reserved.
 */
public interface EventRepository extends CrudRepository<Event, Long> {
}
