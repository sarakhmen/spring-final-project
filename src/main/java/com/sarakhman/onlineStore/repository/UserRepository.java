package com.sarakhman.onlineStore.repository;

import com.sarakhman.onlineStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByUserName(String userName);
    User findUserById(long id);
}