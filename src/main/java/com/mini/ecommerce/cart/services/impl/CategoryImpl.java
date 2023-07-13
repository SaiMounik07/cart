package com.mini.ecommerce.cart.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.exceptionhandler.NoSuchCategoryFound;
import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import com.mini.ecommerce.cart.repositories.CategoryRepo;
import com.mini.ecommerce.cart.services.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;

@Service
public class CategoryImpl implements Product {
   @Autowired
   CategoryRepo categoryRepo;
   @Autowired
   ObjectMapper mapper;
    @Override
    public CreateCategorydto createCategory(@Valid CreateCategoryRq createCategory) throws CategoryAlreadyExists {
        if (createCategory.categoryName==null){
            throw new CategoryAlreadyExists("CategoryName must Not be Null");
        }
        CreateCategoryDb createCategoryDb = new CreateCategoryDb();
        createCategoryDb.setCategoryName(createCategory.categoryName);
        createCategoryDb.setCategoryImageUrl(createCategory.categoryImage);
        createCategoryDb.setCategoryCode(generateCategoryCode(createCategory.categoryName));
        createCategoryDb.setId(UUID.randomUUID().toString().split("-")[0]);
        Optional<CreateCategoryDb> lis=categoryRepo.findBycategoryName(createCategory.categoryName);
        CreateCategorydto createCategorydto;
        if (lis.isEmpty()) {
            CreateCategoryDb categoryDb = categoryRepo.save(createCategoryDb);
            createCategorydto = mapper.convertValue(categoryDb, new TypeReference<CreateCategorydto>() {
            });
            System.out.println(createCategorydto.toString());
        } else {
            throw new CategoryAlreadyExists("category name is exists with categoryName " + createCategoryDb.getCategoryName());
        }
        return createCategorydto;
    }

    public List<CreateCategorydto> getAllCategories(){
        List<CreateCategoryDb> categoryDb=categoryRepo.findAll();
        List<CreateCategorydto> createCategorydto = mapper.convertValue(categoryDb, new TypeReference<>() {
        });
        return createCategorydto;

    }

    @Override
    public CreateCategorydto getSpecficCategory(String categoryName) throws NoSuchCategoryFound {
        if (categoryName!=null) {
            Optional<CreateCategoryDb> createCategoryDb=categoryRepo.findBycategoryName(categoryName);
//            CreateCategoryDb createCategoryDb = categoryRepo.findBycategoryName(categoryName);
            CreateCategorydto createCategorydto = null;
            if (!createCategoryDb.isEmpty()) {
                createCategorydto = mapper.convertValue(createCategoryDb, new TypeReference<CreateCategorydto>() {
                });
            } else {
                throw new NoSuchCategoryFound("the category is not found with name " + categoryName);
            }

            return createCategorydto;
        }
        else {
            throw new NoSuchCategoryFound("The Categeory name is null");
        }
    }

    @Override
    public Boolean deleteCategory(String categoryName) throws CommonException, NoSuchCategoryFound {
        if (categoryName==null||categoryName.equals("null"))
            throw new NoSuchCategoryFound("The Category Name must not be null");
        Optional<CreateCategoryDb> optionalCreateCategoryDb = categoryRepo.findBycategoryName(categoryName);
        if(optionalCreateCategoryDb.isEmpty())
            throw new CommonException("Category Name is invalid");
        else
            categoryRepo.delete(optionalCreateCategoryDb.get());
        Optional<CreateCategoryDb> optionalCreateCategoryDb1 = categoryRepo.findBycategoryName(categoryName);
        if (optionalCreateCategoryDb1.isPresent())
            throw new CommonException("Category "+categoryName+" is not deleted");
        else
            return true;



    }

    @Override
    public CreateCategorydto updateCategory(CreateCategoryRq updateCategory) throws CommonException {
        
        if (updateCategory.categoryName==null||updateCategory.categoryImage==null)
            throw new CommonException("CategoryName or Image is null");
        else {
            Optional <CreateCategoryDb> optionalCreateCategoryDb = categoryRepo.findBycategoryName(updateCategory.categoryName);
            if(optionalCreateCategoryDb.isPresent()) {
                CreateCategoryDb createCategoryDb = optionalCreateCategoryDb.get();
                createCategoryDb.setId(createCategoryDb.getId());
                createCategoryDb.setCategoryName(updateCategory.updatedCategoryName);
                createCategoryDb.setCategoryImageUrl(updateCategory.categoryImage);
                categoryRepo.save(createCategoryDb);
                return mapper.convertValue(createCategoryDb, new TypeReference<CreateCategorydto>() {
                });
            }else {
                throw new CommonException("category name doesnt exists");
            }
        }
    }

    public static String generateCategoryCode(String productName){
        Random random = new Random();
        if (productName!=null)
            return productName.substring(0,3).toUpperCase(Locale.ROOT)+"-"+"60"+random.nextInt(1000);
        else
            return null;
    }

}
