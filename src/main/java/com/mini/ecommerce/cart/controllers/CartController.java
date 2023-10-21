package com.mini.ecommerce.cart.controllers;

import com.mini.ecommerce.cart.dto.request.AddToCartRq;
import com.mini.ecommerce.cart.dto.response.cart.AddToCart;
import com.mini.ecommerce.cart.models.BaseResponse;
import com.mini.ecommerce.cart.services.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@CrossOrigin(origins = "http://localhost:3000/",allowedHeaders = "*")

public class CartController {
    @Autowired
    Cart cart;
    @PostMapping("/cart/addToCart")
    public BaseResponse<AddToCart> addToCart(@RequestHeader(AUTHORIZATION) String token, @RequestBody AddToCartRq addToCartRq){

        return cart.CartLoad(token,addToCartRq);
    }

    @GetMapping("/cart/getItems")
    public void getCartItems(String cartId){

    }

//    @PutMapping("/cart/updateCart")
//    public void updateCart(@RequestBody UpdateCart updateCart){
//
//    }

    @DeleteMapping("/cart/deleteItems")
    public void deleteCartItem(String itemId,String cartId){

    }

    @DeleteMapping("/cart/deleteCart")
    public void deleteCart(String cartId){

    }
}
