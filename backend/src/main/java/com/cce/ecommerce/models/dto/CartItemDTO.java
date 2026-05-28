package com.cce.ecommerce.models.dto;

public class CartItemDTO {
    private Long cartId;
    private Long productId;
    private int quantity;

    public CartItemDTO() {

    }

    public Long getProductId() {
        return productId;
    }

    public Long getCartId() {
        return cartId;
    }

    public int getQuantity() {
        return quantity;
    }
}
