package com.sarakhman.onlineStore.service;

import com.sarakhman.onlineStore.model.Order;
import com.sarakhman.onlineStore.model.Product;
import com.sarakhman.onlineStore.model.Status;
import com.sarakhman.onlineStore.model.User;
import com.sarakhman.onlineStore.repository.OrderRepository;
import com.sarakhman.onlineStore.repository.ProductRepository;
import com.sarakhman.onlineStore.repository.UserRepository;
import com.sarakhman.onlineStore.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Page<Order> findAllOrdersByUserId(long userId, Pageable page){
        return orderRepository.findAllByUserId(userId, page);
    }


    public Page<Order> findAllOrdersByUserIdForGuest(HttpSession session, Pageable pageable) {
        List<Order> orders = (List<Order>)session.getAttribute("guestOrders");
        return PaginationUtil.getPageOf(pageable, orders);
    }

    @Transactional
    public void saveOrder(long userId, long productId){
        Product product = productRepository.findById(productId);
        User user = userRepository.findUserById(userId);
        if(product != null && user != null)
            orderRepository.save(new Order(0, user, product, Status.UNREGISTERED));
    }

    public void saveOrderForGuest(HttpSession session, long productId) {
        List<Order> orders = (List<Order>)session.getAttribute("guestOrders");
        if(orders == null){
            orders = new ArrayList<>();
        }
        Product product = productRepository.findById(productId);
        if(product != null)
            orders.add(new Order(orders.size(), null, product, Status.UNREGISTERED));
        session.setAttribute("guestOrders", orders);
    }

    @Transactional
    public void deleteOrder(long orderId){
        orderRepository.deleteById(orderId);
    }

    public void deleteOrderForGuest(HttpSession session, long orderId){
        List<Order> orders = (List<Order>)session.getAttribute("guestOrders");
        orders.remove((int)orderId);

        for(int i = 0; i < orders.size(); i++){
            orders.get(i).setId(i);
        }
    }

    public void updateOrderStatus(Long orderId, Status newStatus) {
        Order order = orderRepository.findById(orderId);
        if(order != null){
            order.setStatus(newStatus);
            orderRepository.save(order);
        }
    }

    public void saveGuestOrders(HttpSession session, User user) {
        List<Order> orders = (List<Order>)session.getAttribute("guestOrders");
        if(orders != null){
            orders.forEach(order -> {
                order.setUser(user);
                order.setId(0);
            });
            orderRepository.saveAll(orders);
        }
    }
}
