package com.mini.ecommerce.cart.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.CreateProductdto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import com.mini.ecommerce.cart.repositories.CategoryRepo;
import com.mini.ecommerce.cart.services.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@Service
public class CategoryCreationImpl implements Product {
   @Autowired
    CategoryRepo categoryRepo;

   @Autowired
   ObjectMapper mapper;
    @Override
    public CreateCategorydto createCategory(CreateCategoryRq createCategory) throws CategoryAlreadyExists {

        CreateCategoryDb createCategoryDb = new CreateCategoryDb();
        createCategoryDb.setCategoryName(createCategory.categoryName);
        createCategoryDb.setCategoryImageUrl(createCategory.categoryImage);
        createCategoryDb.setCategoryCode(generateCategoryCode(createCategory.categoryName));
        createCategoryDb.setId(UUID.randomUUID().toString().split("-")[0]);
        CreateCategoryDb lis = categoryRepo.findBycategoryName(createCategory.categoryName);
        CreateCategorydto createCategorydto;
        if (lis == null) {
            CreateCategoryDb categoryDb = categoryRepo.save(createCategoryDb);
            createCategorydto = mapper.convertValue(categoryDb, new TypeReference<CreateCategorydto>() {
            });
            System.out.println(createCategorydto.toString());
        } else {
            throw new CategoryAlreadyExists("category name is exists with categorycode" + createCategoryDb.getCategoryCode());
        }
        return createCategorydto;
    }
    public static String generateCategoryCode(String productName){
        Random random = new Random();
        return productName.substring(0,3).toUpperCase(Locale.ROOT)+"-"+"60"+random.nextInt(1000);
    }

}
