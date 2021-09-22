package com.sarakhman.onlineStore.controller;

import com.sarakhman.onlineStore.model.Order;
import com.sarakhman.onlineStore.model.Status;
import com.sarakhman.onlineStore.model.User;
import com.sarakhman.onlineStore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            orderService.saveOrderForGuest(session, productId);
        }
        else{
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
            orderPage = orderService.findAllOrdersByUserIdForGuest(session, pageable);
        }
        else{
            orderPage = orderService.findAllOrdersByUserId(user.getId(), pageable);
        }

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        int totalPages = orderPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "cart";
    }

    @PostMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user == null){
            orderService.deleteOrderForGuest(session, orderId);
        }
        else{
            orderService.deleteOrder(orderId);
        }
        return "redirect:/user/cart/view";
    }

    @PostMapping("/order/{orderId}")
    public String registerOrder(@PathVariable("orderId") Long orderId){
        orderService.updateOrderStatus(orderId, Status.REGISTERED);
        return "redirect:/user/cart/view";
    }
}
