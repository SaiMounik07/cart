package com.mini.ecommerce.cart.repositories;

import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends MongoRepository<CreateCategoryDb, Integer> {
    CreateCategoryDb findBycategoryName(String categoryName);
}
