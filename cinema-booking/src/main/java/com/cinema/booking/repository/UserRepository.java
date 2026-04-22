package com.cinema.booking.repository;

import com.cinema.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    // đúng với ENUM Role
    List<User> findByRole(User.Role role);

    // search tên
    List<User> findByFullNameContainingIgnoreCase(String fullName);

    // email
    User findByEmail(String email);
    
}