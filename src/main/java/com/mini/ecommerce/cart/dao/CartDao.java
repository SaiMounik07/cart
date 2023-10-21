package com.mini.ecommerce.cart.dao;

import com.mini.ecommerce.cart.dto.response.cart.AddToCart;

public interface CartDao {
    AddToCart getPendingCartByCartId(String cartId);
    Boolean saveCartObject(AddToCart addToCart);
}
