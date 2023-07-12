package com.mini.ecommerce.cart.controllers;

import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.request.CreateProduct;
import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import com.mini.ecommerce.cart.repositories.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/product")
public class Product {
    @Autowired
    com.mini.ecommerce.cart.services.Product product;




    @PostMapping("/category")
    public CreateCategorydto createCategory(@RequestBody CreateCategoryRq category) throws CategoryAlreadyExists {
        return product.createCategory(category);
    }

    @GetMapping("/getCategories")
    public void getCategories(){

    }

    @PostMapping("/products")
    public void createProduct(@RequestBody CreateProduct product){

    }

    @GetMapping("/getProducts")
    public void getProducts(){

    }

//    @PutMapping("/updateProduct")
//    public void updateProduct(@RequestBody UpdateProduct updateProduct){
//
//    }
//
//    @PutMapping("/updateCategory")
//    public void updateCategory(@RequestBody UpdateCategory updateCategory){
//
//    }

    @DeleteMapping("/products")
    public void deleteProduct(String productId){

    }

    @DeleteMapping("/category")
    public void deleteCategory(String categoryCode){

    }
}
