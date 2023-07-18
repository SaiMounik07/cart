package com.mini.ecommerce.cart.controllers;

import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.request.CreateProductRq;
import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.CreateProductDto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.exceptionhandler.CommonOkException;
import com.mini.ecommerce.cart.exceptionhandler.NoSuchCategoryFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    com.mini.ecommerce.cart.services.Product product;

    @PostMapping("/category")
    public CreateCategorydto createCategory(@Valid  @RequestBody CreateCategoryRq category) throws CategoryAlreadyExists {
        return product.createCategory(category);
    }

    @GetMapping("/getCategories")
    public List<CreateCategorydto> getAllCategories(){
        return  product.getAllCategories();
    }

    @GetMapping("/category/{categoryName}")
    public CreateCategorydto getSpecficCategory( @PathVariable String categoryName) throws NoSuchCategoryFound {
        return product.getSpecficCategory(categoryName);
    }
    @DeleteMapping("/category/{categoryName}")
    public HashMap<String,Object> deleteCategory(@PathVariable String categoryName) throws CommonException, NoSuchCategoryFound {
        boolean success= product.deleteCategory(categoryName);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("success",success);
        return hashMap;
    }
    @PutMapping("/updateCategory")
    public CreateCategorydto updateCategory(@RequestBody CreateCategoryRq updateCategory) throws CommonException {
        return product.updateCategory(updateCategory);
    }
    @PostMapping("/products")
    public CreateProductDto createProduct(@RequestBody CreateProductRq createProductRq) throws CommonException {
        return product.createProduct(createProductRq);
    }
    @GetMapping("/getProducts")
    public List<CreateProductDto> getProducts(){
        return product.getProducts();
    }
    @GetMapping("/{productDetail}")
    public List<CreateProductDto> getProduct(@PathVariable String productDetail) throws CommonOkException {
        return product.getSpecficProduct(productDetail);
    }
    @PutMapping("/products")
    public CreateProductDto updateProduct(@RequestBody CreateProductRq updateProduct) throws CommonException {
        return product.updateProduct(updateProduct);
    }
    @DeleteMapping("/{productId}")
    public HashMap<String,Object> deleteProduct(@PathVariable  String productId) throws CommonException {
        boolean success=product.deleteProduct(productId);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("success",success);
        return hashMap;
    }
}
