package com.cookiebutter.Services;

import com.cookiebutter.Models.User;
import com.cookiebutter.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by MEUrena on 10/18/16.
 * All rights reserved.
 */

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    @Transactional
    public boolean delete(User user) {
        userRepository.delete(user);
        return true;
    }

}
