package com.mini.ecommerce.cart.repositories.product;

import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends MongoRepository<CreateProductDb,String> {
    Optional<List<CreateProductDb>> findAllByCreatedBy(String userName);
    Optional<List<CreateProductDb>> findAllByProductName(String productName);
    Optional<List<CreateProductDb>> findByProductNameAndCreatedBy(String productName,String userName);
    Optional<CreateProductDb> findByProductIdAndCreatedBy(String productId,String userName);

}
