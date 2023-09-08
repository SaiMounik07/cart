package com.mini.ecommerce.cart.services.impl.productImpl;

import com.mini.ecommerce.cart.dto.request.CreateProductRq;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import com.mini.ecommerce.cart.repositories.product.CategoryRepo;
import com.mini.ecommerce.cart.repositories.product.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
class ProductRequestValidator extends ProductImpl {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    public ProductRequestValidator(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo=categoryRepo;
    }

    public Boolean createProductRequest(CreateProductRq createProductRq,String username) throws CommonException {
        if (createProductRq.getProductId()==null||createProductRq.getProductId().isEmpty()||createProductRq.getProductName()==null||createProductRq.getCategories()==null||createProductRq.getCategories().size()==0||createProductRq.getProductListPrice()==null){
            throw new CommonException("Some fields are null please check");
        }
       Optional<CreateProductDb> createProductDb=productRepo.findByProductIdAndCreatedBy(createProductRq.getProductId(),username);
        if (createProductDb.isPresent()){
            throw new CommonException("Product already exists");
        }
            return true;
    }

    public boolean createProductRequestforUpdate(CreateProductRq createProductRq) throws CommonException {
        if (createProductRq.getProductId()==null||createProductRq.getProductId().isEmpty()||createProductRq.getProductName()==null||createProductRq.getCategories()==null||createProductRq.getCategories().size()==0||createProductRq.getProductListPrice()==null){
            throw new CommonException("Some fields are null please check");
        }
        return true;
    }
}
