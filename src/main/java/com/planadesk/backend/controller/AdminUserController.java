package com.planadesk.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.planadesk.backend.model.User;
import com.planadesk.backend.model.Order;
import com.planadesk.backend.repository.UserRepository;
import com.planadesk.backend.repository.OrderRepository;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AdminUserController(UserRepository userRepository,
                               OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    // ==============================
    // 1️⃣ GET ALL USERS
    // ==============================
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ==============================
    // 2️⃣ SEARCH USERS
    // ==============================
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String q) {
        return userRepository
            .findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                q, q, q);
    }

    // ==============================
    // 3️⃣ GET ORDERS OF A USER
    // ==============================
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersOfUser(@PathVariable String userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Fetch orders by EMAIL
        return orderRepository.findByUserEmail(user.getEmail());
    }

    // ==============================
    // 4️⃣ UPDATE USER (NO PASSWORD)
    // ==============================
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable String userId,
                           @RequestBody User updatedUser) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());
        user.setEnabled(updatedUser.isEnabled());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // ==============================
    // 5️⃣ DELETE USER
    // ==============================
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userRepository.deleteById(userId);
    }
}