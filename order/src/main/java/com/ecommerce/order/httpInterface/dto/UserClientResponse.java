package com.ecommerce.order.httpInterface.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserClientResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private List<AddressClientResponse> addresses;
}
