package com.mini.ecommerce.cart.repositories;

import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends MongoRepository<CreateProductDb,String> {
    Optional<List<CreateProductDb>> findAllByProductName(String productName);
    Optional<CreateProductDb> findByProductId(String productId);

}
