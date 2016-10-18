package com.cookiebutter.Repositories;

import com.cookiebutter.Models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by MEUrena on 10/17/16.
 * All rights reserved.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
