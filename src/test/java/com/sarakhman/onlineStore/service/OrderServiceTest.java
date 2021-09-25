package com.sarakhman.onlineStore.service;

import com.sarakhman.onlineStore.model.Order;
import com.sarakhman.onlineStore.model.Status;
import com.sarakhman.onlineStore.model.User;
import com.sarakhman.onlineStore.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/testDataOrderService.sql"})
public class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    OrderRepository orderRepository;

    @Mock
    HttpSession session;

    private List<Order> guestOrders;

    @Before
    public void initMocks(){
        Page<Order> pageOrders = orderRepository.findAllByUserId(1, PageRequest.of(0, 10));
        guestOrders = new ArrayList<>(pageOrders.toList());
        Mockito.when(session.getAttribute("guestOrders"))
                .thenReturn(guestOrders);
    }

    @Test
    public void shouldSaveCorrectly_WhenSaveGuestOrders(){
        User user = userService.findUserById(1);
        orderService.saveGuestOrders(session, user);
        Page<Order> orders = orderRepository.findAllByUserId(1, PageRequest.of(0, 10));
        Assert.assertEquals(4, orders.getTotalElements());
    }

    @Test
    public void shouldSetNewStatus_WhenUpdateOrderStatus(){
        orderService.updateOrderStatus(1L, Status.PAID);
        Order order = orderRepository.findById(1);
        Assert.assertEquals(Status.PAID, order.getStatus());
    }

    @Test
    public void shouldDeleteCorrectly_WhenDeleteOrderForGuest(){
        Order order = guestOrders.get(0);
        orderService.deleteOrderForGuest(session, 0);
        Assert.assertFalse(guestOrders.contains(order));
    }

    @Test
    public void shouldDeleteCorrectly_WhenDeleteOrder(){
        Order order = orderRepository.findById(1);
        Assert.assertNotNull(order);
        orderService.deleteOrder(1);
        order = orderRepository.findById(1);
        Assert.assertNull(order);
    }

    @Test
    public void shouldSaveCorrectly_WhenSaveOrderForGuest(){
        orderService.saveOrderForGuest(session, 1);
        Assert.assertEquals(3, guestOrders.size());

        Mockito.when(session.getAttribute("guestOrders"))
                .thenReturn(null);
        Mockito.doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            guestOrders = (List<Order>) arguments[1];
            Assert.assertTrue(guestOrders.size() == 1);
            return null;
        }).when(session).setAttribute(Mockito.eq("guestOrders"), Mockito.any());
        orderService.saveOrderForGuest(session, 1);
    }

    @Test
    public void shouldSaveCorrectly_WhenSaveOrder(){
        long sizeBefore = orderRepository.findAllByUserId(1L, PageRequest.of(0, 10)).getTotalElements();
        orderService.saveOrder(1,1);

        long sizeAfter = orderRepository.findAllByUserId(1L, PageRequest.of(0, 10)).getTotalElements();
        Assert.assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    public void shouldReturnPageOfGuestOrders_WhenFindAllOrdersByUserIdForGuest(){
        List<Order> orders = orderService.findAllOrdersByUserIdForGuest(session, PageRequest.of(0, 10))
                .toList();
        Assert.assertEquals(guestOrders, orders);
    }

    @Test
    public void shouldReturnCorrectPage_WhenFindAllOrdersByUserId(){
        long size = orderService.findAllOrdersByUserId(1L, PageRequest.of(0, 10)).getTotalElements();
        Assert.assertEquals(2, size);
    }
}
