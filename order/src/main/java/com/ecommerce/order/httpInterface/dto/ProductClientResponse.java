package com.ecommerce.order.httpInterface.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductClientResponse {
    private String name;
    private String description;
    private BigDecimal price;
    private String pack;
    private Integer stock;
    private String category;
    private String image;
    private Boolean isActive;
}
