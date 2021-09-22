package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.*;
import com.sarakhman.onlineStore.service.OrderService;
import com.sarakhman.onlineStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/management")
public class AdminManagementController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

    @GetMapping("/cart/view/{userId}")
    public String showCart(@PathVariable("userId") long userId, Model model,
                           @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                           HttpServletRequest request){
        HttpSession session = request.getSession();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Order> orderPage = orderService.findAllOrdersByUserId(userId, pageable);

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        session.setAttribute("cartUserId", userId);

        int totalPages = orderPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "adminCart";
    }

    @PostMapping("/cart/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId, HttpServletRequest request){
        HttpSession session = request.getSession();
        orderService.deleteOrder(orderId);
        return "redirect:/admin/management/cart/view/" + session.getAttribute("cartUserId");
    }

    @PostMapping("/cart/order/{orderId}/{status}")
    public String setOrderStatus(@PathVariable("orderId") Long orderId, @PathVariable("status") Status status,
                                HttpServletRequest request){
        HttpSession session = request.getSession();
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/management/cart/view/" + session.getAttribute("cartUserId");
    }

    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable("productId") Long productId ,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        orderService.saveOrder(user.getId(), productId);

        return "redirect:/admin/catalog";
    }

    @GetMapping
    public String managementView(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                 HttpServletRequest request){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userService.findAllUsers(pageable);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "adminManagement";
    }

    @PostMapping("/block")
    public String setUserActiveStatus(@RequestParam("userId") long userId){
        userService.banHandler(userId);
        return "redirect:/admin/management";
    }
}
