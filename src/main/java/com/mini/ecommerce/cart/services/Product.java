package com.mini.ecommerce.cart.services;

import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.request.CreateProductRq;
import com.mini.ecommerce.cart.dto.response.product.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.exceptionhandler.CommonOkException;
import com.mini.ecommerce.cart.exceptionhandler.NoSuchCategoryFound;
import com.mini.ecommerce.cart.models.BaseResponse;
import java.io.IOException;
import java.util.List;

public interface Product {

 CreateCategorydto createCategory(CreateCategoryRq createCategory,String token) throws CategoryAlreadyExists;
 BaseResponse<?> getAllCategories(String token);
 CreateCategorydto getSpecficCategory(String categoryName,String token) throws NoSuchCategoryFound;
 Boolean deleteCategory(String categoryName,String token) throws CommonException, NoSuchCategoryFound;
 CreateCategorydto updateCategory(CreateCategoryRq updateCategory,String token) throws CommonException, IOException;
 CreateProductDto createProduct(CreateProductRq createProductRq,String token) throws CommonException;
 BaseResponse<List<CreateProductDto>> getProducts(String token);
 List<CreateProductDto> getSpecficProduct(String productDetail,String token) throws CommonOkException;
 Boolean deleteProduct(String productId,String token) throws CommonException;
 CreateProductDto updateProduct(CreateProductRq updateProduct,String token) throws CommonException, ClassNotFoundException;
}
