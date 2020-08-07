package ru.javamentor.springboot.DAO;

import org.springframework.stereotype.Component;
import ru.javamentor.springboot.model.Role;
import ru.javamentor.springboot.model.User;
import ru.javamentor.springboot.repository.RoleRepository;
import ru.javamentor.springboot.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserHibernateDAO implements IUserDAO {
    public final UserRepository userRepository;
    public final RoleRepository roleRepository;

    public UserHibernateDAO(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public boolean addUser(String name, String email, String password) {
        try {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(password);
            if (!checkUserExist(name)) {
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editUser(Long id, String name, String email, String password, String[] roles) {
        try {
            Optional<User> userFind = userRepository.findById(id);
            if (userFind.isPresent()) {
                User user = userFind.get();
                user.setEmail(email);
                user.setPassword(password);
                userRepository.save(user);
                if (deleteAllRolesUser(user)) {
                    for (String nameRole : roles) {
                        Role role = new Role();
                        role.setName(nameRole);
                        role.setUser(user);
                        roleRepository.save(role);
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean deleteAllRolesUser(User user) {
        try {
            List<Role> roles = getRolesByUser(user);
            for (Role role : roles) {
                roleRepository.delete(role);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Transactional
    public boolean deleteUser(Long id) {
        try {
            Optional<User> userFind = userRepository.findById(id);
            if (userFind.isPresent()) {
                User user = userFind.get();
                List<Role> roles = getRolesByUser(user);
                for (Role role : roles) {
                    roleRepository.delete(role);
                }
                userRepository.delete(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean userIsAdmin(String name, String password) {
        Optional<User> user = userRepository.findByNameAndPassword(name, password);
        if (checkUserAdmin(user)) return true;
        return false;
    }

    private boolean checkUserAdmin(Optional<User> user) {
        if (user.isPresent()) {
            List<Role> roles = roleRepository.findByUser(user.get());
            for (Role role : roles) {
                if (role.getName().toLowerCase().equals("admin")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public User getUserByNameAndPassword(String name, String password) {
        try {
            Optional<User> user = userRepository.findByNameAndPassword(name, password);
            if (user.isPresent()) {
                return user.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean userIsAdmin(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (checkUserAdmin(user)) return true;
        return false;
    }

    @Override
    public User getUserByName(String name) {
        try {
            Optional<User> user = userRepository.findByName(name);
            if (user.isPresent()) {
                return user.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Role> getRolesByUser(User user) {
        List<Role> roles = new ArrayList<>();
        roles = roleRepository.findByUser(user);
        return roles;
    }

    @Override
    public User getUserById(Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                return user.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            users = userRepository.findAll();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkUserExist(String name) {
        try {
            Optional<User> user = userRepository.findByName(name);
            if (user.isPresent()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getAllRoles() {
        List<String> users = new ArrayList<>();
        try {
            users = roleRepository.findRoles();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllNamesRoles() {
        List<Role> usersRoles = new ArrayList<>();
        List<String> names = new ArrayList<>();
        try {
            usersRoles = roleRepository.findAll();
            for (Role role : usersRoles) {
                if (!names.contains(role.getName())) {
                    names.add(role.getName());
                }
            }
            return names;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addRolesUser(User user, String[] roles) {
        try {
            userRepository.save(user);
            for (String name : roles) {
                Role role = new Role();
                role.setName(name);
                role.setUser(user);
                roleRepository.save(role);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<String> getRolesNamesByUser(User user) {
        List<String> names = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        roles = roleRepository.findByUser(user);
        for (Role role : roles) {
            names.add(role.getName());
        }
        return names;
    }
}