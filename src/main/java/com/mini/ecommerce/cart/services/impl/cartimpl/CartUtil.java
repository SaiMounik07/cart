package com.mini.ecommerce.cart.services.impl.cartimpl;

import com.mini.ecommerce.cart.dto.request.AddToCartRq;

public class CartUtil {
    public boolean validateCartRq(AddToCartRq addToCartRq, String cartId) {
        if (addToCartRq.getCartId().equals(cartId)&&!addToCartRq.getProductId().isEmpty())
            return true;
        return false;
    }
}
