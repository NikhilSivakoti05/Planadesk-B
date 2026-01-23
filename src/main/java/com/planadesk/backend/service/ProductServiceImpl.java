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
    public Product createProduct(String name, String description,
                                 int globalStock,
                                 List<String> countryCodes,
                                 List<Double> prices,
                                 MultipartFile[] images) {

        List<String> imageUrls = uploadImages(images);
        List<CountryPrice> pricesList = buildPrices(countryCodes, prices);

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setGlobalStock(globalStock);
        product.setImageUrls(imageUrls);
        product.setCountryPrices(pricesList);

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String productId, String name, String description,
                                 int globalStock,
                                 List<String> countryCodes,
                                 List<Double> prices,
                                 MultipartFile[] images) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(name);
        product.setDescription(description);
        product.setGlobalStock(globalStock);
        product.setCountryPrices(buildPrices(countryCodes, prices));

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

    private List<CountryPrice> buildPrices(List<String> codes,
                                           List<Double> prices) {

        if (codes == null || prices == null)
            throw new RuntimeException("Country data missing");

        if (codes.size() != prices.size())
            throw new RuntimeException("Country and price counts must match");

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
        if (images == null || images.length == 0) return urls;

        try {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String url = imageKitService.upload(file);
                    urls.add(url);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
        return urls;
    }
}
