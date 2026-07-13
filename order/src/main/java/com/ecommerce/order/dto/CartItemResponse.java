package com.ecommerce.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private String name;
    private String description;
    private BigDecimal price;
    private String pack;
    private Integer quantity;
    private String category;
    private String image;
}
