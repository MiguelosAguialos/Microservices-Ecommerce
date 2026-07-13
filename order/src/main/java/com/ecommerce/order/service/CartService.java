package com.ecommerce.order.service;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.dto.CartResponse;
import com.ecommerce.order.httpInterface.ProductHttpInterface;
import com.ecommerce.order.httpInterface.UserHttpInterface;
import com.ecommerce.order.httpInterface.dto.ProductClientResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductHttpInterface productHttpInterface;
    private final UserHttpInterface userHttpInterface;

    public CartResponse addProductToCart(String userId, CartItemRequest cartItemRequest) {
        validateUser(userId);
        ProductClientResponse product = getAvailableProduct(cartItemRequest.getProductId());

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(userId, cartItemRequest.getProductId());
        if(existingCartItem != null){
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem item = new CartItem();
            item.setUser(userId);
            item.setProduct(cartItemRequest.getProductId());
            item.setQuantity(cartItemRequest.getQuantity());
            cartItemRepository.save(item);
        }
        return mapCartToCartResponse(cartItemRepository.findByUser(userId));
    }

    public CartResponse getCartItems(String userId) {
        validateUser(userId);
        return mapCartToCartResponse(cartItemRepository.findByUser(userId));
    }

    public CartResponse removeProductFromCart(String userId, Long productId){
        validateUser(userId);
        getAvailableProduct(productId);

        CartItem cartItem = cartItemRepository.findByUserAndProduct(userId, productId);
        if(cartItem == null) throw new RuntimeException("Cart item not found");

        cartItemRepository.delete(cartItem);
        return mapCartToCartResponse(cartItemRepository.findByUser(userId));
    }

    public void clearCart(String userId){
        cartItemRepository.deleteAll(cartItemRepository.findByUser(userId));
    }

    private void validateUser(String userId){
        userHttpInterface.getUserById(userId);
    }

    private ProductClientResponse getAvailableProduct(Long productId) {
        ProductClientResponse product = productHttpInterface.getProductById(productId);
        if (Boolean.FALSE.equals(product.getIsActive()) || product.getStock() == null || product.getStock() <= 0) {
            throw new RuntimeException("Product not found");
        }
        return product;
    }

    private CartResponse mapCartToCartResponse(List<CartItem> cartItems){
        CartResponse cartItemResponse = new CartResponse();
        BigDecimal totalPrice = BigDecimal.ZERO;
        cartItemResponse.setCartItems(cartItems.stream().map(this::mapCartItemToCartItemResponse).toList());
        for(CartItem item : cartItems){
            ProductClientResponse product = productHttpInterface.getProductById(item.getProduct());
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        cartItemResponse.setTotalPrice(totalPrice);
        return cartItemResponse;
    }

    private CartItemResponse mapCartItemToCartItemResponse(CartItem cartItem){
        ProductClientResponse product = productHttpInterface.getProductById(cartItem.getProduct());
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setName(product.getName());
        cartItemResponse.setPrice(product.getPrice());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setPack(product.getPack());
        cartItemResponse.setImage(product.getImage());
        cartItemResponse.setCategory(product.getCategory());
        cartItemResponse.setDescription(product.getDescription());
        return cartItemResponse;
    }
}
