package com.example.vendor.repository;

import com.example.vendor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail (String email);

    User findFirstByEmail(String email);
}
