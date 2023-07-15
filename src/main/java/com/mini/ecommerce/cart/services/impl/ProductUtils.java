package com.mini.ecommerce.cart.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.CreateProductDto;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import com.mini.ecommerce.cart.repositories.CategoryRepo;
import com.mini.ecommerce.cart.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductUtils extends ProductImpl{
    List<CreateCategorydto> categoryList=new ArrayList<>();
    @Autowired
    ObjectMapper mapper;
    @Autowired
    CategoryRepo categoryRepo;
    public ProductUtils(ProductRepo productRepo,CategoryRepo categoryRepo,ObjectMapper mapper){
        this.categoryRepo=categoryRepo;
        this.productRepo=productRepo;
        this.mapper=mapper;
    }
    public List<CreateCategorydto> mapCategory(List<String> categories) throws CommonException {
        for (String categoryName:categories){
            Optional <CreateCategoryDb> optionalCreateCategoryDb = categoryRepo.findBycategoryName(categoryName);
            if (optionalCreateCategoryDb.isPresent()){
                new CreateCategoryDb();
                CreateCategoryDb createCategoryDb;
                createCategoryDb=optionalCreateCategoryDb.get();
                CreateCategorydto createCategorydto=mapper.convertValue(createCategoryDb, new TypeReference<CreateCategorydto>() {
                });
                if (createCategorydto.getProducts()==null){
                    createCategorydto.setProducts(null);
                }
                categoryList.add(createCategorydto);
            }else {
                throw new CommonException("Category not exists");
            }
        }
        return categoryList;
    }

    public Boolean isProductOutOfStock(Integer initialStocks, Integer replishedStocks) {
        if (initialStocks==null){
            return true;
        }else {
            if (initialStocks-replishedStocks<=0){
                return true;
            }
        }
        return false;
    }

}
