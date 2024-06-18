package com.ivanov.microservice_project.repository;

import com.ivanov.microservice_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Optional<User> findCustomerByUsername(String username);

    Optional<User> findUserByEmail(String username);
}
