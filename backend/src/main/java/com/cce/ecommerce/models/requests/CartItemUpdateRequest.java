package com.cce.ecommerce.models.requests;

public class CartItemUpdateRequest {
    private long productId;
    private int quantity;

    public CartItemUpdateRequest() {

    }

    public int getQuantity() {
        return quantity;
    }

    public long getProductId() {
        return productId;
    }
}
