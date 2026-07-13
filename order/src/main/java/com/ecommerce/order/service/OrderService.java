package com.ecommerce.order.service;

import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.httpInterface.ProductHttpInterface;
import com.ecommerce.order.httpInterface.UserHttpInterface;
import com.ecommerce.order.httpInterface.dto.ProductClientResponse;
import com.ecommerce.order.repository.CartItemRepository;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductHttpInterface productHttpInterface;
    private final UserHttpInterface userHttpInterface;

    @Transactional
    public OrderResponse createOrder(String userId) {
        userHttpInterface.getUserById(userId);

        List<CartItem> cartItems = cartItemRepository.findByUser(userId);
        if (cartItems.isEmpty()) throw new RuntimeException("No items in cart");

        Order order = new Order();
        order.setUser(userId);
        order.setTotalAmount(cartItems.stream()
                .map(cartItem -> getProductPrice(cartItem.getProduct()).multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setStatus(OrderStatus.CONFIRMED);

        List<OrderItem> orderItems = cartItems.stream().map(this::mapCartItemToOrderItem).toList();
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        order.setOrderItems(orderItems);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        cartService.clearCart(userId);

        return mapOrderToOrderResponse(order);
    }

    private OrderItem mapCartItemToOrderItem(CartItem cartItem){
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(getProductPrice(cartItem.getProduct()));
        return orderItem;
    }

    private BigDecimal getProductPrice(Long productId) {
        ProductClientResponse product = productHttpInterface.getProductById(productId);
        return product.getPrice();
    }

    private OrderItemDTO mapOrderItemToOrderItemDTO(OrderItem orderItem){
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        BeanUtils.copyProperties(orderItem, orderItemDTO);
        orderItemDTO.setSubTotal(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        orderItemDTO.setProductId(orderItem.getProduct());
        return orderItemDTO;
    }

    private OrderResponse mapOrderToOrderResponse(Order order){
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(order, orderResponse);
        orderResponse.setOrderItems(order.getOrderItems().stream().map(this::mapOrderItemToOrderItemDTO).toList());
        return orderResponse;
    }
}
