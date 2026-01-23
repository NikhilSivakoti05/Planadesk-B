package com.planadesk.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.planadesk.backend.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
