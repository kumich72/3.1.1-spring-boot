package ru.javamentor.springboot.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.javamentor.springboot.dto.UserRole;
import ru.javamentor.springboot.exception.DBException;
import ru.javamentor.springboot.model.Role;
import ru.javamentor.springboot.model.User;
import ru.javamentor.springboot.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping(value = "user")
    @ResponseBody
    public ModelAndView printCurrentUser() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        User user = userService.getUserByNameAndPassword(username, password);
        List<Role> roles = userService.getRolesByUser(user);
        ModelAndView result = new ModelAndView("user");
        result.addObject(roles);
        result.addObject(user);
        return result;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "users")
    @ResponseBody
    public ModelAndView printUsers(Model model) {
        List<UserRole> userRoles = userService.getAllUsersAndRoles();
        model.addAttribute("userRoles", userRoles);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("userRoles", userRoles);
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(value = "/delete")
    @ResponseBody
    public RedirectView deleteUser(@RequestParam String id) {
        RedirectView redirectView = new RedirectView("users");
        try {
            userService.deleteUser(Long.valueOf(id));

        } catch (DBException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return redirectView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(value = "/editing")
    @ResponseBody
    public ModelAndView editingUser(@RequestParam String edit_id) {
        User user = userService.getUserById(Long.valueOf(edit_id));
        Map<String, Boolean> roles = userService.getRoleCheckedByUser(user);
        ModelAndView result = new ModelAndView("editUser");
        result.addObject("user", user);
        result.addObject("roles", roles);
        return result;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(value = "/edit")
    @ResponseBody
    public RedirectView editUser(@RequestParam String id, String name, String password, String email, String[] roles) {
        ModelAndView result = new ModelAndView("users");
        try {
            userService.editUser(Long.valueOf(id), name, password, email, roles);
        } catch (DBException e) {
            result.addObject("error", e.getLocalizedMessage());
        }
        return new RedirectView("users");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(value = "/add")
    @ResponseBody
    public RedirectView addUser(@RequestParam String name, String password, String email, String[] roles) {
        ModelAndView result = new ModelAndView("users");
        try {
            User user = new User(name, password, email);
            userService.addRolesUser(user, roles);
        } catch (Exception e) {
            result.addObject("error", e.getLocalizedMessage());
        }
        List<User> users = userService.getAllUsers();

        result.addObject("users", users);
        return new RedirectView("users");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "addUser")
    public String printAdd(ModelMap model) {
        List<String> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);
        return "addUser";
    }

    @GetMapping(value = "hello")
    public String printWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello!");
        messages.add("I'm Spring MVC-SECURITY application");
        messages.add("5.2.0 version by sep'19 ");
        model.addAttribute("messages", messages);
        return "hello";
    }

    @GetMapping(value = "login")
    public String loginPage() {
        return "login";
    }
}