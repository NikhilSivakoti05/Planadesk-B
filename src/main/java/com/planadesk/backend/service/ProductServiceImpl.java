package com.planadesk.backend.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.planadesk.backend.model.*;
import com.planadesk.backend.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ImageKitService imageKitService;

    public ProductServiceImpl(ProductRepository productRepository,
                              ImageKitService imageKitService) {
        this.productRepository = productRepository;
        this.imageKitService = imageKitService;
    }

    @Override
    public Product createProduct(
            String name,
            String description,
            int globalStock,
            List<String> countryCodes,
            List<Double> prices,
            List<Integer> sections,
            MultipartFile[] images) {

        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setGlobalStock(globalStock);
        product.setCountryPrices(buildPrices(countryCodes, prices));
        product.setImageUrls(uploadImages(images));
        product.setSections(sections != null ? sections : new ArrayList<>());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(
            String productId,
            String name,
            String description,
            int globalStock,
            List<String> countryCodes,
            List<Double> prices,
            List<Integer> sections,
            MultipartFile[] images) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(name);
        product.setDescription(description);
        product.setGlobalStock(globalStock);
        product.setCountryPrices(buildPrices(countryCodes, prices));

        if (sections != null) {
            product.setSections(sections);
        }

        if (images != null && images.length > 0) {
            product.setImageUrls(uploadImages(images));
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ---------- Helpers ----------

    private List<CountryPrice> buildPrices(List<String> codes, List<Double> prices) {

        if (codes == null || prices == null || codes.size() != prices.size()) {
            throw new RuntimeException("Country & price mismatch");
        }

        List<CountryPrice> list = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            CountryPrice cp = new CountryPrice();
            cp.setCountryCode(codes.get(i));
            cp.setPrice(prices.get(i));
            list.add(cp);
        }
        return list;
    }

    private List<String> uploadImages(MultipartFile[] images) {

        List<String> urls = new ArrayList<>();
        if (images == null) return urls;

        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                urls.add(imageKitService.upload(file));
            }
        }
        return urls;
    }
}