package com.planadesk.backend.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.planadesk.backend.model.Product;

public interface ProductService {

    Product createProduct(
        String name,
        String description,
        int globalStock,
        List<String> countryCodes,
        List<Double> prices,
        List<Integer> sections,
        MultipartFile[] images
    );

    Product updateProduct(
        String id,
        String name,
        String description,
        int globalStock,
        List<String> countryCodes,
        List<Double> prices,
        List<Integer> sections,
        MultipartFile[] images
    );

    void deleteProduct(String id);

    List<Product> getAllProducts();

    Product getProductById(String id);
}