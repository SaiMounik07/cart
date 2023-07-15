package com.mini.ecommerce.cart.services.impl;

import com.mini.ecommerce.cart.dto.request.CreateProductRq;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import com.mini.ecommerce.cart.repositories.CategoryRepo;
import com.mini.ecommerce.cart.repositories.ProductRepo;
import com.mini.ecommerce.cart.services.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
class ProductRequestValidator {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    public ProductRequestValidator(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo=categoryRepo;
    }

    public Boolean createProductRequest(CreateProductRq createProductRq) throws CommonException {
        if (createProductRq.getProductId()==null||createProductRq.getProductId().isEmpty()||createProductRq.getProductName()==null||createProductRq.getCategories()==null||createProductRq.getCategories().size()==0||createProductRq.getProductListPrice()==null){
            throw new CommonException("Some fields are null please check");
        }
       Optional<CreateProductDb> createProductDb=productRepo.findByProductId(createProductRq.getProductId());
        if (createProductDb.isPresent()){
            throw new CommonException("Product already exists");
        }
            return true;
    }
}
