package ru.javamentor.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.javamentor.springboot.DAO.UserHibernateDAO;
import ru.javamentor.springboot.dto.UserRole;
import ru.javamentor.springboot.exception.DBException;
import ru.javamentor.springboot.model.Role;
import ru.javamentor.springboot.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService, UserDetailsService {

    private static UserService userService;
    @Autowired
    private UserHibernateDAO userHibernateDAO;

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<Role> roles = getRolesByUser(user);
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(userName,
                        user.getPassword(),
                        roles);

        return userDetails;
    }

    @Override
    public User findByUsername(String name) {
        try {
            User user = userHibernateDAO.getUserByName(name);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Role> getRolesByUser(User user) {
        try {
            List<Role> roles = userHibernateDAO.getRolesByUser(user);
            return roles;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean userIsAdmin(User user) {
        try {
            List<Role> roles = userHibernateDAO.getRolesByUser(user);
            boolean isHaveAdmin = roles.stream().anyMatch(e -> e.getName().equals("ADMIN"));
            return isHaveAdmin;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public User getUserById(Long id) {
        try {
            User user = userHibernateDAO.getUserById(id);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            users = userHibernateDAO.getAllUsers();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<UserRole> getAllUsersAndRoles() {
        List<User> users = new ArrayList<>();
        List<UserRole> userRoles = new ArrayList<>();
        try {
            users = userHibernateDAO.getAllUsers();
            for (User user : users) {
                List<Role> roles = getRolesByUser(user);
                UserRole userRole = new UserRole(user, roles);
                userRoles.add(userRole);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userRoles;
    }

    @Override
    public List<String> getAllRoles() {
        List<String> roleArrayList = new ArrayList<>();
        try {
            roleArrayList = userHibernateDAO.getAllRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleArrayList;
    }


    @Override
    @Transactional
    public boolean addRolesUser(User user, String[] roles) {
        try {
            if (userHibernateDAO.addRolesUser(user, roles)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Boolean> getRoleCheckedByUser(User user) {
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        List<String> rolesUser = getRolesNamesByUser(user);
        List<String> allNamesRoles = getAllRolesNames();

        for (String role : allNamesRoles) {
            result.put(role, rolesUser.contains(role));
        }
        return result;
    }

    private List<String> getRolesNamesByUser(User user) {
        List<String> roleArrayList = new ArrayList<>();
        try {
            roleArrayList = userHibernateDAO.getRolesNamesByUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleArrayList;
    }

    private List<String> getAllRolesNames() {
        List<String> roleArrayList = new ArrayList<>();
        try {
            roleArrayList = userHibernateDAO.getAllNamesRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleArrayList;
    }

    @Transactional
    public boolean deleteUser(Long id) throws DBException {
        try {
            if (userHibernateDAO.deleteUser(id)) {
                return true;
            }
        } catch (Exception e) {
            throw new DBException(e);
        }
        return false;
    }

    @Transactional
    public boolean addUser(User user) throws DBException {
        try {
            if (userHibernateDAO.addUser(user)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Transactional
    public boolean editUser(Long id, String name, String password, String email, String[] roles) throws DBException {
        try {
            if (userHibernateDAO.editUser(id, name, password, email, roles)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public boolean userIsAdmin(String name, String password) throws DBException {
        try {
            if (userHibernateDAO.userIsAdmin(name, password)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public User getUserByNameAndPassword(String name, String password) {
        try {
            User user = userHibernateDAO.getUserByNameAndPassword(name, password);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean userIsAdmin(Long id) throws DBException {
        try {
            if (userHibernateDAO.userIsAdmin(id)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new DBException(e);
        }
    }
}