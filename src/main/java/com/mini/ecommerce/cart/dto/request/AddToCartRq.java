package com.mini.ecommerce.cart.dto.request;

import lombok.Data;

@Data
public class AddToCartRq {
    private String cartId;
    private String productId;
    private Integer quantity;
}
