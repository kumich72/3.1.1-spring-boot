package ru.javamentor.springboot.dto;

import ru.javamentor.springboot.model.Role;
import ru.javamentor.springboot.model.User;

import java.util.List;

public class UserRole {
    private ru.javamentor.springboot.model.User User;
    private List<Role> roles;

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public UserRole(User user, List<Role> roles) {
        User = user;
        this.roles = roles;
    }

    public UserRole() {
    }
}