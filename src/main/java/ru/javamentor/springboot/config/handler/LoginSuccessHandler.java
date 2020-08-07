package ru.javamentor.springboot.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.javamentor.springboot.model.Role;
import ru.javamentor.springboot.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        List<Role> roles = (List<Role>) authentication.getAuthorities();
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");

        HttpSession session = httpServletRequest.getSession();

        session.setAttribute("username", username);
        session.setAttribute("password", password);

        boolean isHaveAdmin = roles.stream().anyMatch(e -> e.getName().equals("ADMIN"));
        if(isHaveAdmin){
            httpServletResponse.sendRedirect("/users");
        }else{
            boolean isHaveUser = roles.stream().anyMatch(e -> e.getName().equals("USER"));
            if(isHaveUser) {
                User user = (User) session.getAttribute("user");
                session.setAttribute("user", user);

                httpServletResponse.sendRedirect("/user");
            }
        }
    }
}