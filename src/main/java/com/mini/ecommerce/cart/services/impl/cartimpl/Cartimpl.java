package com.mini.ecommerce.cart.services.impl.cartimpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dao.CartDao;
import com.mini.ecommerce.cart.dto.response.product.ProductForCategory;
import com.mini.ecommerce.cart.outboundRequest.ProductFeign;
import com.mini.ecommerce.cart.config.JwtService;
import com.mini.ecommerce.cart.dto.request.AddToCartRq;
import com.mini.ecommerce.cart.dto.response.cart.AddToCart;
import com.mini.ecommerce.cart.dto.response.member.Role;
import com.mini.ecommerce.cart.models.BaseResponse;
import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import com.mini.ecommerce.cart.repositories.cart.CartRepo;
import com.mini.ecommerce.cart.repositories.member.MemberRepo;
import com.mini.ecommerce.cart.repositories.product.ProductRepo;
import com.mini.ecommerce.cart.services.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Cartimpl implements Cart {
    @Autowired
    JwtService jwtService;
    @Autowired
    MemberRepo memberRepo;
    CartUtil cartUtil;

    @Autowired
    ProductFeign productFeign;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    CartDao cartDao;
    AddToCart addToCart;
    AddToCart pendingCart;
    @Autowired
    ObjectMapper mapper;

    List list;
    @Override
    public BaseResponse<AddToCart> CartLoad(String token, AddToCartRq addToCartRq) {
        if (Boolean.TRUE.equals(validateToken(token))) {
          cartUtil=new CartUtil();
          list=new ArrayList<>();
          String cartId=jwtService.extractEmail(token.substring(7));
          if (cartUtil.validateCartRq(addToCartRq,cartId)){
             Optional<CreateProductDb> createProductDb=productRepo.findByProductIdAndCreatedBy(addToCartRq.getProductId(),cartId);
             if (createProductDb.isEmpty()){
                 return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),"false",false,"product is not available",null);
             }
              ProductForCategory product=mapper.convertValue(createProductDb.get(), new TypeReference<ProductForCategory>() {
              });

             if (Objects.nonNull(product)){
                 list.add(product);
                 pendingCart=cartDao.getPendingCartByCartId(cartId);
                 if (Objects.nonNull(pendingCart)) {
                     list.addAll(pendingCart.getProducts());
                     int totalQuantities = pendingCart.getQuantity() + addToCartRq.getQuantity();
                     double totalPrice=pendingCart.getTotalPrice()+createProductDb.get().getProductOfferPrice();
                     addToCart=AddToCart.builder().cartId(cartId).products(list).quantity(totalQuantities).totalPrice(totalPrice).createdAt(Calendar.getInstance().getTime()).build();
                 }else
                    addToCart=AddToCart.builder().cartId(cartId).products(list).quantity(addToCartRq.getQuantity()).totalPrice(createProductDb.get().getProductOfferPrice()).createdAt(Calendar.getInstance().getTime()).build();
                 if (cartDao.saveCartObject(addToCart)) {
                     return new BaseResponse<>(HttpStatus.ACCEPTED.value(), "true", true, null,
                             AddToCart.builder().cartId(cartId).products(list).quantity(addToCartRq.getQuantity()).totalPrice(createProductDb.get().getProductOfferPrice()).createdAt(Calendar.getInstance().getTime()).build());
                 }
             }

          }else
              return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),"false",false,"some fields are null",null);
        }
        return  null;
    }

    private Boolean validateToken(String token) {
        String userName = jwtService.extractEmail(token.substring(7));
        var userDetails = memberRepo.findByEmail(userName);
        if (userDetails.get().getRole().equals(Role.ADMIN) || userDetails.get().getRole().equals(Role.SUPERADMIN)) {
            return true;
        }
        return false;
    }
}