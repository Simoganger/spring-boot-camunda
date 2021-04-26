package com.foo.spring.boot.camunda.model;

import java.util.List;

public class User {

    private String username;
    private List<String> groups;

    public User() {
    }

    public User(String username, List<String> groups) {
        this.username = username;
        this.groups = groups;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", groups=" + groups +
                '}';
    }
}
