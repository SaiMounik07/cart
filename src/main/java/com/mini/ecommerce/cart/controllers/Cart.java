package com.mini.ecommerce.cart.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class Cart {

//    @PostMapping("/cart/createCart")
//    public void addToCart(@RequestBody CreateCart cart){
//
//    }

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
