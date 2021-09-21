package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.User;
import com.sarakhman.onlineStore.service.OrderService;
import com.sarakhman.onlineStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/invalid-login")
    public String invalidateSession(RedirectAttributes redirectAttributes, HttpServletRequest request){
        request.getSession().invalidate();
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        redirectAttributes.addAttribute("error", true);
        return "redirect:/login";
    }

    @PostMapping("/login/process")
    public String logConfirm(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            Optional<User> user = userService.findUserByEmail(currentUserEmail);
            if(session != null && user.isPresent()){
                orderService.saveGuestOrders(session, user.get());
                session.setAttribute("user", user.get());
            }
        }

        return "redirect:/catalog";
    }


}
