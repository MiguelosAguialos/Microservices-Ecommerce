package com.ecommerce.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private List<CartItemResponse> cartItems;
    private BigDecimal totalPrice;
}
