package com.ecommerce.order.httpInterface;

import com.ecommerce.order.httpInterface.dto.UserClientResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/users")
public interface UserHttpInterface {
    @GetExchange("/{id}")
    UserClientResponse getUserById(@PathVariable String id);
}
