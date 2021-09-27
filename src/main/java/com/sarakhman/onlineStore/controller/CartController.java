package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.Order;
import com.sarakhman.onlineStore.model.Status;
import com.sarakhman.onlineStore.model.User;
import com.sarakhman.onlineStore.service.OrderService;
import com.sarakhman.onlineStore.util.PaginationUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Log4j2
@Controller
@RequestMapping("/user/cart")
public class CartController {

    @Autowired
    OrderService orderService;

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable("productId") Long productId ,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user == null){
            log.info("Saving order for guest");
            orderService.saveOrderForGuest(session, productId);
        }
        else{
            log.info("Saving order for authorized user");
            orderService.saveOrder(user.getId(), productId);
        }
        return "redirect:/user/catalog";
    }

    @GetMapping("/view")
    public String showCart(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                           HttpServletRequest request) {
        HttpSession session = request.getSession();
        Pageable pageable = PageRequest.of(page - 1, size);
        User user = (User)session.getAttribute("user");
        Page<Order> orderPage;
        if(user == null){
            log.info("Finding all orders for guest");
            orderPage = orderService.findAllOrdersByUserIdForGuest(session, pageable);
        }
        else{
            log.info("Finding all orders for authorized user");
            orderPage = orderService.findAllOrdersByUserId(user.getId(), pageable);
        }

        PaginationUtil.setPaginationAttributes(model, orderPage, page, size);

        return "cart";
    }

    @PostMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user == null){
            log.info("Deleting order for guest");
            orderService.deleteOrderForGuest(session, orderId);
        }
        else{
            log.info("Deleting order for authorized user");
            orderService.deleteOrder(orderId);
        }
        return "redirect:/user/cart/view";
    }

    @PostMapping("/order/{orderId}")
    public String registerOrder(@PathVariable("orderId") Long orderId){
        orderService.updateOrderStatus(orderId, Status.REGISTERED);
        log.info("Order successfully registered");
        return "redirect:/user/cart/view";
    }
}
