package com.mini.ecommerce.cart.services.impl.productImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.request.CreateProductRq;
import com.mini.ecommerce.cart.dto.response.product.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.exceptionhandler.CommonOkException;
import com.mini.ecommerce.cart.exceptionhandler.NoSuchCategoryFound;
import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import com.mini.ecommerce.cart.repositories.product.CategoryRepo;
import com.mini.ecommerce.cart.repositories.product.ProductRepo;
import com.mini.ecommerce.cart.services.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Service
public class ProductImpl implements Product {
   @Autowired
   CategoryRepo categoryRepo;

   @Autowired
   ProductRepo productRepo;
   @Autowired
   ObjectMapper mapper;

    @Override
    public CreateProductDto createProduct(CreateProductRq createProductRq) throws CommonException {
        CreateProductDto createProductDto = null;
        ProductRequestValidator productRequestValidator=new ProductRequestValidator(productRepo,categoryRepo);
        ProductUtils productUtils = new ProductUtils(productRepo,categoryRepo,mapper);
        if (productRequestValidator.createProductRequest(createProductRq)) {
            CreateProductDb createProductDb = new CreateProductDb();
            Integer replishedStocks = 0;//need to implement
            Integer reservedStocks = 0;//need to implement
            createProductDb.setProductId(createProductRq.getProductId());
            createProductDb.setProductDescription(createProductRq.getProductDescription());
            createProductDb.setProductName(createProductRq.getProductName());
            createProductDb.setProductImage(createProductRq.getProductImage());
            createProductDb.setInitialStocks(createProductRq.getInitialStocks());
            createProductDb.setCategories(productUtils.mapCategory(createProductRq.getCategories()));
            createProductDb.setProductListPrice(createProductRq.getProductListPrice());
            createProductDb.setProductOfferPrice(createProductRq.getProductOfferPrice());
            createProductDb.setIsActive(createProductRq.getIsActive());
            createProductDb.setIsOutOfStock(productUtils.isProductOutOfStock(createProductRq.getInitialStocks(), replishedStocks));
            createProductDb.setTotalStockReplenished(replishedStocks);
            createProductDb.setReservedStock(reservedStocks);
            createProductDb.setId(UUID.randomUUID().toString().split("-")[0]);
            createProductDb.setSuccess(true);
            CreateProductDb createProductDb1 = productRepo.save(createProductDb);

           Optional<CreateCategoryDb> createCategoryDb;
            for (String category: createProductRq.getCategories()){

                createCategoryDb=categoryRepo.findBycategoryName(category);
                List<CreateProductDto> list = null;
                    if (createCategoryDb.isPresent()) {
                        try {
                            list = createCategoryDb.get().getProducts();
                            list.add(mapper.convertValue(createProductDb1, new TypeReference<>() {
                            }));

                            CreateCategoryDb categoryDb = createCategoryDb.get();
                            categoryDb.setProducts(list);
                            categoryRepo.save(categoryDb);
                        }
                        catch (Exception e){
                            list = new ArrayList<>();
                            list.add(mapper.convertValue(createProductDb1, new TypeReference<>() {
                            }));
                            CreateCategoryDb categoryDb = createCategoryDb.get();
                            categoryDb.setProducts(list);
                            categoryRepo.save(categoryDb);
                        }


                    } else {
                        throw new CommonException("category invalid");
                    }
            }
            createProductDto = mapper.convertValue(createProductDb1, new TypeReference<CreateProductDto>() {
            });

        }else {
            throw new CommonException("an Error occured");
        }
        return createProductDto;
    }

    @Override
    public List<CreateProductDto> getProducts() {
       List<CreateProductDb> createProductDb=productRepo.findAll();
       List<CreateProductDto> createProductDto=mapper.convertValue(createProductDb, new TypeReference<>() {
        });
       return createProductDto;
    }

    @Override
    public List<CreateProductDto> getSpecficProduct(String productDetail) throws CommonOkException {
       Optional<List<CreateProductDb>> createProductDb=productRepo.findAllByProductName(productDetail);
       if (createProductDb.isEmpty()||createProductDb.get().size()==0){
           throw new CommonOkException("the product details are empty");
       }
       return mapper.convertValue(createProductDb.get(), new TypeReference<List<CreateProductDto>>() {
       });
    }

    @Override
    public Boolean deleteProduct(String productId) throws CommonException {
        if (productId==null){
            throw new CommonException("productId is null");
        }
        Optional<CreateProductDb> createProductDb=productRepo.findByProductId(productId);
        if (createProductDb.isEmpty()) {
            throw new CommonException("product not exist");
        }
        productRepo.delete(createProductDb.get());
        return true;
    }

    @Override
    public CreateProductDto updateProduct(CreateProductRq createProductRq) throws CommonException, ClassNotFoundException {
        CreateProductDto createProductDto = null;
        Optional<CreateProductDb> createProductDb2=productRepo.findByProductId(createProductRq.getProductId());
        if(createProductDb2.isEmpty()){
            throw new CommonException("product does not exists");
        }else {
            List<CreateCategorydto> oldCategories=createProductDb2.get().getCategories();
            ProductRequestValidator productRequestValidator = new ProductRequestValidator(productRepo, categoryRepo);
            ProductUtils productUtils = new ProductUtils(productRepo, categoryRepo, mapper);
            if (productRequestValidator.createProductRequestforUpdate(createProductRq)) {
                CreateProductDb createProductDb = createProductDb2.get();
                Integer replishedStocks = 0;//need to implement
                Integer reservedStocks = 0;//need to implement
                createProductDb.setProductDescription(createProductRq.getProductDescription());
                createProductDb.setProductName(createProductRq.getProductName());
                createProductDb.setProductImage(createProductRq.getProductImage());
                createProductDb.setInitialStocks(createProductRq.getInitialStocks());
                createProductDb.setCategories(productUtils.mapCategory(createProductRq.getCategories()));
                createProductDb.setProductListPrice(createProductRq.getProductListPrice());
                createProductDb.setProductOfferPrice(createProductRq.getProductOfferPrice());
                createProductDb.setIsActive(createProductRq.getIsActive());
                createProductDb.setIsOutOfStock(productUtils.isProductOutOfStock(createProductRq.getInitialStocks(), replishedStocks));
                createProductDb.setTotalStockReplenished(replishedStocks);
                createProductDb.setReservedStock(reservedStocks);
                createProductDb.setSuccess(true);
                CreateProductDb createProductDb1 = productRepo.save(createProductDb);
                Optional<CreateCategoryDb> createCategoryDb;
                List<CreateProductDto> list = new ArrayList<>();
                for (String category : createProductRq.getCategories()) {
                    createCategoryDb = categoryRepo.findBycategoryName(category);
                    if (createCategoryDb.isPresent()) {
                        for (CreateCategorydto categoryDb : oldCategories) {
                            if (!(createProductRq.getCategories().contains(categoryDb.getCategoryName()))) {
                                productUtils.deleteProductFromCategory(categoryDb, createProductDb1);
                            }
                        }
                        list=productUtils.updateProductDetailsInCategory(createCategoryDb.get().getProducts(),createProductDb);
                        list.add(mapper.convertValue(createProductDb1, new TypeReference<>() {
                        }));
                        CreateCategoryDb categoryDb = createCategoryDb.get();
                        categoryDb.setProducts(list);
                        categoryRepo.save(categoryDb);
                    }
                            else {
                        throw new CommonException("category invalid");
                    }
                }

                createProductDto = mapper.convertValue(createProductDb1, new TypeReference<CreateProductDto>() {
                });
            } else {
                throw new CommonException("some error occured");
            }
        }
        return createProductDto;
    }


    @Override
    public CreateCategorydto createCategory(@Valid CreateCategoryRq createCategory) throws CategoryAlreadyExists {
        if (createCategory.categoryName==null){
            throw new CategoryAlreadyExists("CategoryName must Not be Null");
        }
        ProductUtils productUtils=new ProductUtils(productRepo,categoryRepo,mapper);
        CreateCategoryDb createCategoryDb = new CreateCategoryDb();
        createCategoryDb.setCategoryName(createCategory.categoryName);
        createCategoryDb.setCategoryImageUrl(createCategory.categoryImage);
        createCategoryDb.setCategoryCode(generateCategoryCode(createCategory.categoryName));
        createCategoryDb.setId(UUID.randomUUID().toString().split("-")[0]);
//        createCategoryDb.setProducts(productUtils.mapProducts(createCategory.getCategoryName()));
        Optional<CreateCategoryDb> lis=categoryRepo.findBycategoryName(createCategory.categoryName);

        CreateCategorydto createCategorydto;
        if (lis.isEmpty()) {
            CreateCategoryDb categoryDb = categoryRepo.save(createCategoryDb);
            createCategorydto = mapper.convertValue(categoryDb, new TypeReference<>() {
            });

        } else {
            throw new CategoryAlreadyExists("category name is exists with categoryName " + createCategoryDb.getCategoryName());
        }
        return createCategorydto;
    }

    public List<CreateCategorydto> getAllCategories(){
        List<CreateCategoryDb> categoryDb=categoryRepo.findAll();
        return mapper.convertValue(categoryDb, new TypeReference<>() {
        });

    }

    @Override
    public CreateCategorydto getSpecficCategory(String categoryName) throws NoSuchCategoryFound {
        if (categoryName!=null) {
            Optional<CreateCategoryDb> createCategoryDb=categoryRepo.findBycategoryName(categoryName);
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
        else {
            ProductUtils productUtils = new ProductUtils(productRepo,categoryRepo,mapper);
            productUtils.categoriesFromAllProduct(optionalCreateCategoryDb.get());
            categoryRepo.delete(optionalCreateCategoryDb.get());
        }
        Optional<CreateCategoryDb> optionalCreateCategoryDb1 = categoryRepo.findBycategoryName(categoryName);
        if (optionalCreateCategoryDb1.isPresent())
            throw new CommonException("Category "+categoryName+" is not deleted");
        else
            return true;

    }

    @Override
    public CreateCategorydto updateCategory(@NotNull CreateCategoryRq updateCategory) throws CommonException, IOException {
        ProductUtils productUtils = new ProductUtils(productRepo,categoryRepo,mapper);
        if (updateCategory.categoryName==null||updateCategory.updatedCategoryName==null||updateCategory.updatedCategoryName.length()<3)
            throw new CommonException("CategoryName or Image is null");
        else {
            Optional <CreateCategoryDb> optionalCreateCategoryDb = categoryRepo.findBycategoryName(updateCategory.categoryName);
            if(optionalCreateCategoryDb.isPresent()) {
                CreateCategoryDb createCategoryDb = optionalCreateCategoryDb.get();
                if(updateCategory.categoryImage==null)
                    createCategoryDb.setCategoryImageUrl(createCategoryDb.getCategoryImageUrl());
                else
                    createCategoryDb.setCategoryImageUrl(updateCategory.categoryImage);
                createCategoryDb.setId(createCategoryDb.getId());
                createCategoryDb.setCategoryName(updateCategory.updatedCategoryName);
                categoryRepo.save(createCategoryDb);
                return mapper.convertValue(createCategoryDb, new TypeReference<CreateCategorydto>() {
                });
            }else {
                throw new CommonException("category name doesn't exists");
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
