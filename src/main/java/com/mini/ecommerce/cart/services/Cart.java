package com.mini.ecommerce.cart.services;

import com.mini.ecommerce.cart.dto.request.AddToCartRq;
import com.mini.ecommerce.cart.dto.response.cart.AddToCart;
import com.mini.ecommerce.cart.models.BaseResponse;

public interface Cart {
    BaseResponse<AddToCart> CartLoad(String token, AddToCartRq addToCartRq);
}
