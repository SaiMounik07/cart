package com.mini.ecommerce.cart.repositories.cart;

import com.mini.ecommerce.cart.models.documents.CartDb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepo extends MongoRepository<CartDb,String> {
   Optional<CartDb> findByCartId(String cartId);
}
