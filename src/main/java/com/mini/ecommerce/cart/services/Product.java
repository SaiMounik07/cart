package com.mini.ecommerce.cart.services;

import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.CreateProductdto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.exceptionhandler.NoSuchCategoryFound;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

public interface Product {

public CreateCategorydto createCategory(CreateCategoryRq createCategory) throws CategoryAlreadyExists;

public List<CreateCategorydto> getAllCategories();

public CreateCategorydto getSpecficCategory(String categoryName) throws NoSuchCategoryFound;

public Boolean deleteCategory(String categoryName) throws CommonException, NoSuchCategoryFound;

public CreateCategorydto updateCategory(CreateCategoryRq updateCategory) throws CommonException;
}
