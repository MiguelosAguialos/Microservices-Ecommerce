package com.ecommerce.order.httpInterface;

import com.ecommerce.order.httpInterface.dto.ProductClientResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/products")
public interface ProductHttpInterface {
    @GetExchange("/{id}")
    ProductClientResponse getProductById(@PathVariable Long id);
}
