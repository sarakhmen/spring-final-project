package com.sarakhman.onlineStore.handler;

import com.sarakhman.onlineStore.model.User;
import com.sarakhman.onlineStore.service.OrderService;
import com.sarakhman.onlineStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;


/**
 * Redirects user to correct page depending on it roles
 */
@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String currentUserEmail = authentication.getName();
        Optional<User> user = userService.findUserByEmail(currentUserEmail);
        if(user.isPresent()){
            orderService.saveGuestOrders(session, user.get());
            session.setAttribute("user", user.get());
        }

        if (authorities.contains("ADMIN")) {
            response.sendRedirect("/admin/catalog");
        }
        else {
            response.sendRedirect("/user/catalog");
        }
    }
}