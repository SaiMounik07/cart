package com.mini.ecommerce.cart.controllers;

import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.request.CreateProductRq;
import com.mini.ecommerce.cart.dto.response.product.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.exceptionhandler.CommonOkException;
import com.mini.ecommerce.cart.exceptionhandler.NoSuchCategoryFound;
import com.mini.ecommerce.cart.models.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    com.mini.ecommerce.cart.services.Product product;

    /**
     *
     * @param category
     * @param token
     * @return CreateCategory
     * @throws CategoryAlreadyExists
     */
    @PostMapping("/category")
    public CreateCategorydto createCategory(@Valid  @RequestBody CreateCategoryRq category,@RequestHeader(AUTHORIZATION) String token) throws CategoryAlreadyExists {
        return product.createCategory(category,token);
    }

    /**
     *
     * @param token
     * @return
     */

    @GetMapping("/getCategories")
    public BaseResponse<?> getAllCategories(@RequestHeader(AUTHORIZATION) String token){
        return  product.getAllCategories(token);
    }

    /**
     *
     * @param categoryName
     * @param token
     * @return
     * @throws NoSuchCategoryFound
     */
    @GetMapping("/category")
    public CreateCategorydto getSpecficCategory( @RequestHeader("categoryName") String categoryName,@RequestHeader(AUTHORIZATION) String token) throws NoSuchCategoryFound {
        return product.getSpecficCategory(categoryName,token);
    }

    /**
     *
     * @param categoryName
     * @param token
     * @return
     * @throws CommonException
     * @throws NoSuchCategoryFound
     */
    @DeleteMapping("/category/{categoryName}")
    public HashMap<String,Object> deleteCategory(@PathVariable String categoryName,@RequestHeader(AUTHORIZATION) String token) throws CommonException, NoSuchCategoryFound {
        boolean success= product.deleteCategory(categoryName,token);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("success",success);
        return hashMap;
    }

    /**
     *
     * @param updateCategory
     * @param token
     * @return
     * @throws CommonException
     * @throws IOException
     */
    @PutMapping("/updateCategory")
    public CreateCategorydto updateCategory(@RequestBody CreateCategoryRq updateCategory,@RequestHeader(AUTHORIZATION) String token) throws CommonException, IOException {
        return product.updateCategory(updateCategory,token);
    }

    /**
     *
     * @param createProductRq
     * @param token
     * @return
     * @throws CommonException
     */
    @PostMapping("/products")
    public CreateProductDto createProduct(@RequestBody CreateProductRq createProductRq,@RequestHeader(AUTHORIZATION) String token) throws CommonException {
        return product.createProduct(createProductRq,token);
    }

    /**
     *
     * @param token
     * @return
     */
    @GetMapping("/getProducts")
    public BaseResponse<List<CreateProductDto>> getProducts(@RequestHeader(AUTHORIZATION) String token){
        return product.getProducts(token);
    }

    /**
     *
     * @param productDetail
     * @param token
     * @return
     * @throws CommonOkException
     */
    @GetMapping("/product")
    public List<CreateProductDto> getProduct(@RequestHeader("productName") String productDetail,@RequestHeader(AUTHORIZATION) String token) throws CommonOkException {
        return product.getSpecficProduct(productDetail,token);
    }

    /**
     *
     * @param updateProduct
     * @param token
     * @return
     * @throws CommonException
     * @throws ClassNotFoundException
     */
    @PutMapping("/products")
    public CreateProductDto updateProduct(@RequestBody CreateProductRq updateProduct,@RequestHeader(AUTHORIZATION) String token) throws CommonException, ClassNotFoundException {
        return product.updateProduct(updateProduct,token);
    }

    /**
     *
     * @param productId
     * @param token
     * @return
     * @throws CommonException
     */
    @DeleteMapping("/{productId}")
    public HashMap<String,Object> deleteProduct(@PathVariable  String productId,@RequestHeader(AUTHORIZATION) String token) throws CommonException {
        boolean success=product.deleteProduct(productId,token);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("success",success);
        return hashMap;
    }
}
