package com.mini.ecommerce.cart.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.CreateProductDto;
import com.mini.ecommerce.cart.dto.response.ProductForCategory;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import com.mini.ecommerce.cart.repositories.CategoryRepo;
import com.mini.ecommerce.cart.repositories.ProductRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.IntStream;

public class ProductUtils extends ProductImpl {
    List<CreateCategorydto> categoryList = new ArrayList<>();
    @Autowired
    ObjectMapper mapper;
    @Autowired
    CategoryRepo categoryRepo;

    public ProductUtils(ProductRepo productRepo, CategoryRepo categoryRepo, ObjectMapper mapper) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.mapper = mapper;
    }

    public List<CreateCategorydto> mapCategory(List<String> categories) throws CommonException {
        for (String categoryName : categories) {
            Optional<CreateCategoryDb> optionalCreateCategoryDb = categoryRepo.findBycategoryName(categoryName);
            if (optionalCreateCategoryDb.isPresent()) {
                new CreateCategoryDb();
                CreateCategoryDb createCategoryDb;
                createCategoryDb = optionalCreateCategoryDb.get();
                CreateCategorydto createCategorydto = mapper.convertValue(createCategoryDb, new TypeReference<CreateCategorydto>() {
                });
                if (createCategorydto.getProducts() == null) {
                    createCategorydto.setProducts(null);
                }
                categoryList.add(createCategorydto);
            } else {
                throw new CommonException("Category not exists");
            }
        }
        return categoryList;
    }

    public Boolean isProductOutOfStock(Integer initialStocks, Integer replishedStocks) {
        if (initialStocks == null) {
            return true;
        } else {
            if (initialStocks - replishedStocks <= 0) {
                return true;
            }
        }
        return false;
    }
    public List<CreateProductDto> updateProductDetailsInCategory(List<CreateProductDto> products, CreateProductDb createProductDb) {
        if (products!=null)
            products.removeIf(id -> Objects.equals(id.getProductId(), createProductDb.getProductId()));
        else {
            List<CreateProductDto> list = new ArrayList<>();
            products=list;
        }
        return products;
    }
    public void categoriesFromAllProduct(CreateCategoryDb createCategoryDb) throws CommonException {
          List<CreateProductDto> list=createCategoryDb.getProducts();
          if (list==null){
              return;
          }
          for (CreateProductDto product:list) {
              String productId = product.getProductId();
              Optional<CreateProductDb> createProductDb = productRepo.findByProductId(productId);
              List<CreateCategorydto> listOfCategories;
              CreateProductDb createProductDb1;
              if (createProductDb.isEmpty()) {
                  return;
              } else {
                  createProductDb1 = createProductDb.get();
                  listOfCategories = createProductDb1.getCategories();
                  IntStream.range(0, listOfCategories.size()).forEach(i -> {
                      if (listOfCategories.get(i).getCategoryCode().equals(createCategoryDb.getCategoryCode())) {
                          listOfCategories.remove(listOfCategories.get(i));
                      }
                  });
              }
              createProductDb1.setCategories(listOfCategories);
              productRepo.save(createProductDb1);
          }
          }

    public void deleteProductFromCategory( CreateCategorydto createCategoryDb, CreateProductDb createProductDb1)  {
        Iterator<ProductForCategory> iterator = createCategoryDb.getProducts().iterator();
        while (iterator.hasNext()) {
            ProductForCategory product = iterator.next();
            if (product.getProductId().equals(createProductDb1.getProductId())) {
                iterator.remove();
                break;
            }
        }
        CreateCategoryDb createCategoryDb1=mapper.convertValue(createCategoryDb, new TypeReference<CreateCategoryDb>() {
        });
        categoryRepo.save(createCategoryDb1);
    }
}
