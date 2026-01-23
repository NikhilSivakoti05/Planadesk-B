package com.planadesk.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.planadesk.backend.model.Product;
import com.planadesk.backend.service.ProductService;

@RestController
@RequestMapping("/api/products")

public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam int globalStock,
        @RequestParam List<String> countryCodes,
        @RequestParam List<Double> prices,
        @RequestParam MultipartFile[] images
    ) {
        return productService.createProduct(
            name, description, globalStock, countryCodes, prices, images);
    }

    @PutMapping("/{id}")
    public Product updateProduct(
        @PathVariable String id,
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam int globalStock,
        @RequestParam List<String> countryCodes,
        @RequestParam List<Double> prices,
        @RequestParam(required = false) MultipartFile[] images
    ) {
        return productService.updateProduct(
            id, name, description, globalStock, countryCodes, prices, images);
    }
    
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProductById(id);
    }
}
