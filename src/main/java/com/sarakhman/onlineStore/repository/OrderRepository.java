package com.sarakhman.onlineStore.repository;

import com.sarakhman.onlineStore.model.Order;
import com.sarakhman.onlineStore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByUser(User user, Pageable pageable);
    Page<Order> findAllByUserId(long id, Pageable pageable);
    void deleteById(long id);
    Order findById(long id);
}
