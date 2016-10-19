package com.cookiebutter.Models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MEUrena on 10/17/16.
 * All rights reserved.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    @Column
    @Size(min = 3, max = 25)
    private String name;
    @Column
    @Size(min = 2, max = 25)
    private String lastname;
    @Column
    @NotNull
    private String email;
    @Column
    @NotNull
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CustomEvent> customEvents = new ArrayList<>();

    public User() {
    }

    public User(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CustomEvent> getCustomEvents() {
        return customEvents;
    }

    public void setCustomEvents(List<CustomEvent> customEvents) {
        this.customEvents = customEvents;
    }
}
