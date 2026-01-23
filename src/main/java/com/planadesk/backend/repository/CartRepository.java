package com.planadesk.backend.repository;

import com.planadesk.backend.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {

    
    Optional<Cart> findByUserEmail(String userEmail);

    
    void deleteByUserEmail(String userEmail);
}
