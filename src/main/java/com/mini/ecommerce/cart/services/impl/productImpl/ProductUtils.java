package com.mini.ecommerce.cart.services.impl.productImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dto.response.product.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import com.mini.ecommerce.cart.dto.response.product.ProductForCategory;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import com.mini.ecommerce.cart.repositories.product.CategoryRepo;
import com.mini.ecommerce.cart.repositories.product.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

    public List<CreateCategorydto> mapCategory(List<String> categories,String userName) throws CommonException {
        for (String categoryName : categories) {
            Optional<CreateCategoryDb> optionalCreateCategoryDb = categoryRepo.findByCategoryNameAndCreatedBy(categoryName,userName);
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
    public void categoriesFromAllProduct(CreateCategoryDb createCategoryDb,String userName) throws CommonException {
          List<CreateProductDto> list=createCategoryDb.getProducts();
          if (list==null){
              return;
          }
          for (CreateProductDto product:list) {
              String productId = product.getProductId();
              Optional<CreateProductDb> createProductDb = productRepo.findByProductIdAndCreatedBy(productId,userName);
              List<CreateCategorydto> listOfCategories;
              CreateProductDb createProductDb1;
              if (createProductDb.isEmpty()) {
                  return;
              } else {
                  createProductDb1 = createProductDb.get();
                  listOfCategories = createProductDb1.getCategories();

                  for (int i = 0; i < listOfCategories.size(); i++) {
                      if (listOfCategories.get(i).getCategoryCode().equals(createCategoryDb.getCategoryCode())) {
                          listOfCategories.remove(i);
                          break;
                      }
                  }
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
