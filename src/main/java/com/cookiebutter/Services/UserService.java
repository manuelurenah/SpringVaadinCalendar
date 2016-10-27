package com.cookiebutter.Services;

import com.cookiebutter.Models.User;
import com.cookiebutter.Repositories.UserRepository;
import com.vaadin.server.VaadinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by MEUrena on 10/18/16.
 * All rights reserved.
 */

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        return (User) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("current_user");
    }

    public User findOne() {
        return userRepository.findOne((long) 1);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean exists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
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
