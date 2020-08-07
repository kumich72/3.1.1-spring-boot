package ru.javamentor.springboot.service;

import ru.javamentor.springboot.dto.UserRole;
import ru.javamentor.springboot.model.Role;
import ru.javamentor.springboot.model.User;
import java.util.List;
import java.util.Map;

public interface IUserService {
    List<User> getAllUsers();

    User findByUsername(String name);

    List<Role> getRolesByUser(User user);

    boolean userIsAdmin(User user);

    List<UserRole> getAllUsersAndRoles();

    List<String> getAllRoles();

    boolean addRolesUser(User user, String[] roles);

    Map<String, Boolean> getRoleCheckedByUser(User user);
}