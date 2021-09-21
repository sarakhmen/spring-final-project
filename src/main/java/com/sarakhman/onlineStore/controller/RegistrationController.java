package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.User;
import com.sarakhman.onlineStore.service.OrderService;
import com.sarakhman.onlineStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "signup";
    }

    @PostMapping("/signup")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model, HttpServletRequest request)
            throws ServletException {
        HttpSession session = request.getSession();
        Optional<User> userExist = userService.findUserByEmail(user.getEmail());
        if (userExist.isPresent()) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "*There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        String preCryptPassword = user.getPassword();
        user = userService.saveUser(user);
        request.login(user.getEmail(), preCryptPassword);
        request.getSession().setAttribute("user", user);
        orderService.saveGuestOrders(session, user);
        return "redirect:/catalog";
    }

}
