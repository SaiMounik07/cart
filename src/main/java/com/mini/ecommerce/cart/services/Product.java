package com.mini.ecommerce.cart.services;

import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.CreateProductdto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import org.springframework.stereotype.Service;

public interface Product {

public CreateCategorydto createCategory(CreateCategoryRq createCategory) throws CategoryAlreadyExists;


}
