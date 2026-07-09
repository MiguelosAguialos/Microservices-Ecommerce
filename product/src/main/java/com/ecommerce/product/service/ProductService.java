package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        productRequest.setIsActive(true);
        return mapProductToProductResponse(productRepository.save(mapProductRequestToProduct(productRequest)));
    }

    public List<ProductResponse> fetchActiveProducts() {
        return productRepository.findByIsActiveTrue().stream().map(this::mapProductToProductResponse).toList();
    }

    public List<ProductResponse> fetchAllProducts() {
        return productRepository.findAll().stream().map(this::mapProductToProductResponse).toList();
    }

    public List<ProductResponse> searchProducts(String text) {
        return productRepository.searchProducts(text).stream().map(this::mapProductToProductResponse).toList();
    }

    public Optional<ProductResponse> fetchProductById(Long id) {
        return productRepository.findById(id).map(this::mapProductToProductResponse);
    }

    public Optional<Boolean> updateProduct(Long id, ProductRequest productRequest){
        return productRepository.findById(id)
                .map(existingProduct -> {
                    BeanUtils.copyProperties(productRequest, existingProduct, "id");
                    productRepository.save(existingProduct);
                    return true;
                });
    }

    public Optional<Boolean> deleteProduct(Long id){
        return productRepository.findById(id).map(existingProduct -> {
            productRepository.delete(existingProduct);
            return true;
        });
    }

    private ProductResponse mapProductToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        return productResponse;
    }

    private Product mapProductRequestToProduct(ProductRequest productRequest) {
        Product product = new Product();
        BeanUtils.copyProperties(productRequest, product);
        return product;
    }




}
