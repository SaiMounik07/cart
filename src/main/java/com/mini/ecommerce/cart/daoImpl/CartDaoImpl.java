package com.mini.ecommerce.cart.daoImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dao.CartDao;
import com.mini.ecommerce.cart.dto.response.cart.AddToCart;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.documents.CartDb;
import com.mini.ecommerce.cart.repositories.cart.CartRepo;
import com.mini.ecommerce.cart.services.impl.memberImpl.MemberImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service

public class CartDaoImpl implements CartDao {
    private static final Logger log = Logger.getLogger(CartDaoImpl.class.getName());
    @Autowired
    CartRepo cartRepo;
    @Autowired
    ObjectMapper objectMapper;


    public AddToCart getPendingCartByCartId(String cartId){
        try{
            Optional<CartDb> cartDb=this.cartRepo.findByCartId(cartId);
            if (cartDb.isPresent())
                return objectMapper.convertValue(cartDb.get(), new TypeReference<AddToCart>() {
                });

        }catch (Exception e){
            throw new CommonException("cart ID not found");
        }
        return null;
    }

    @Override
    public Boolean saveCartObject(AddToCart addToCart) {
        try {
            cartRepo.save(objectMapper.convertValue(addToCart, new TypeReference<CartDb>() {
            }));
            return true;
        }catch (Exception e){
            log.info(e.getMessage());
            throw  new CommonException("cart is not saved");
        }
    }

}
