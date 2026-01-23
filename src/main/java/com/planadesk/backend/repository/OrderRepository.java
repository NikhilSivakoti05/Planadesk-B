package com.planadesk.backend.repository;

import com.planadesk.backend.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByUserEmailOrderByOrderDateDesc(String email);

    List<Order> findAllByOrderByOrderDateDesc();
}
