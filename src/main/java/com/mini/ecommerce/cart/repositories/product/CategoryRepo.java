package com.mini.ecommerce.cart.repositories.product;

import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends MongoRepository<CreateCategoryDb, String> {
    Optional<CreateCategoryDb> findBycategoryName(String categoryName);

}
