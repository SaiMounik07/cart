package com.mini.ecommerce.cart.services.impl.productImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.config.JwtService;
import com.mini.ecommerce.cart.dto.request.CreateCategoryRq;
import com.mini.ecommerce.cart.dto.request.CreateProductRq;
import com.mini.ecommerce.cart.dto.response.member.Role;
import com.mini.ecommerce.cart.dto.response.product.CategoryforProductDto;
import com.mini.ecommerce.cart.dto.response.product.CreateCategorydto;
import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.exceptionhandler.CommonOkException;
import com.mini.ecommerce.cart.exceptionhandler.NoSuchCategoryFound;
import com.mini.ecommerce.cart.models.BaseResponse;
import com.mini.ecommerce.cart.models.documents.CreateCategoryDb;
import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import com.mini.ecommerce.cart.repositories.member.MemberRepo;
import com.mini.ecommerce.cart.repositories.product.CategoryRepo;
import com.mini.ecommerce.cart.repositories.product.ProductRepo;
import com.mini.ecommerce.cart.services.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductImpl implements Product {
   @Autowired
   CategoryRepo categoryRepo;

   @Autowired
   ProductRepo productRepo;
   @Autowired
   ObjectMapper mapper;
   @Autowired
    JwtService jwtService;
   @Autowired
    MemberRepo memberRepo;
    static Random random;

    @Override
    public CreateProductDto createProduct(CreateProductRq createProductRq,String token) throws CommonException {
        if (Boolean.TRUE.equals(validateToken(token))) {
            CreateProductDto createProductDto = null;
            ProductRequestValidator productRequestValidator = new ProductRequestValidator(productRepo, categoryRepo);
            ProductUtils productUtils = new ProductUtils(productRepo, categoryRepo, mapper);
            if (Boolean.TRUE.equals(productRequestValidator.createProductRequest(createProductRq,jwtService.extractEmail(token.substring(7))))) {
                CreateProductDb createProductDb = new CreateProductDb();
                Integer replishedStocks = 0;//need to implement
                Integer reservedStocks = 0;//need to implement
                BeanUtils.copyProperties(createProductRq,createProductDb);
                createProductDb.setCategories(productUtils.mapCategory(createProductRq.getCategories(),jwtService.extractEmail(token.substring(7))));
                createProductDb.setIsOutOfStock(productUtils.isProductOutOfStock(createProductRq.getInitialStocks(), replishedStocks));
                createProductDb.setTotalStockReplenished(replishedStocks);
                createProductDb.setReservedStock(reservedStocks);
                createProductDb.setId(UUID.randomUUID().toString().split("-")[0]);
                createProductDb.setSuccess(true);
                createProductDb.setCreatedBy(jwtService.extractEmail(token.substring(7)));
                createProductDb.setCreatedAt(Calendar.getInstance().getTime());
                CreateProductDb createProductDb1 = productRepo.save(createProductDb);
                Optional<CreateCategoryDb> createCategoryDb;
                for (String category : createProductRq.getCategories()) {

                    createCategoryDb = categoryRepo.findByCategoryNameAndCreatedBy(category,jwtService.extractEmail(token.substring(7)));
                    List<CreateProductDto> list = null;
                    if (createCategoryDb.isPresent()) {
                        try {
                            list = createCategoryDb.get().getProducts();
                            list.add(mapper.convertValue(createProductDb1, new TypeReference<>() {
                            }));

                            CreateCategoryDb categoryDb = createCategoryDb.get();
                            categoryDb.setProducts(list);
                            categoryRepo.save(categoryDb);
                        } catch (Exception e) {
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

            } else {
                throw new CommonException("an Error occured");
            }
            return createProductDto;
        }else {
            throw new CommonException("un authorized");
        }
    }

    @Override
    public BaseResponse<List<CreateProductDto>> getProducts(String token) {
      Optional<List<CreateProductDb>> createProductDb=productRepo.findAllByCreatedBy(jwtService.extractEmail(token.substring(7)));
        return new BaseResponse<>(HttpStatus.OK.value(),"success",true,null, mapper.convertValue(createProductDb.get(), new TypeReference<>() {
         }));
    }

    @Override
    public List<CreateProductDto> getSpecficProduct(String productDetail,String token) throws CommonOkException {

       Optional<List<CreateProductDb>> createProductDb=productRepo.findByProductNameAndCreatedBy(productDetail,jwtService.extractEmail(token.substring(7)));
       if (createProductDb.isEmpty()||createProductDb.get().isEmpty()){
           throw new CommonOkException("the product details are empty");
       }
       return mapper.convertValue(createProductDb.get(), new TypeReference<List<CreateProductDto>>() {
       });
    }

    @Override
    public Boolean deleteProduct(String productId,String token) throws CommonException {
        if (validateToken(token)) {

            if (productId == null) {
                throw new CommonException("productId is null");
            }
            Optional<CreateProductDb> createProductDb = productRepo.findByProductIdAndCreatedBy(productId, jwtService.extractEmail(token.substring(7)));
            if (createProductDb.isEmpty()) {
                throw new CommonException("product not exist");
            }
            productRepo.delete(createProductDb.get());
            return true;
        }else {
            throw new CommonException("un authorized");
        }
    }

    @Override
    public CreateProductDto updateProduct(CreateProductRq createProductRq,String token) throws CommonException {
        if (Boolean.TRUE.equals(validateToken(token))) {
            CreateProductDto createProductDto = null;
            Optional<CreateProductDb> createProductDb2 = productRepo.findByProductIdAndCreatedBy(createProductRq.getProductId(),jwtService.extractEmail(token.substring(7)));
            if (createProductDb2.isEmpty()) {
                throw new CommonException("product does not exists");
            } else {
                List<CreateCategorydto> oldCategories = createProductDb2.get().getCategories();
                ProductRequestValidator productRequestValidator = new ProductRequestValidator(productRepo, categoryRepo);
                ProductUtils productUtils = new ProductUtils(productRepo, categoryRepo, mapper);
                if (productRequestValidator.createProductRequestforUpdate(createProductRq)) {
                    CreateProductDb createProductDb = createProductDb2.get();
                    Integer replishedStocks = 0;//need to implement
                    Integer reservedStocks = 0;//need to implement
                    BeanUtils.copyProperties(createProductRq,createProductDb);
                    createProductDb.setCategories(productUtils.mapCategory(createProductRq.getCategories(),jwtService.extractEmail(token.substring(7))));
                    createProductDb.setIsOutOfStock(productUtils.isProductOutOfStock(createProductRq.getInitialStocks(), replishedStocks));
                    createProductDb.setTotalStockReplenished(replishedStocks);
                    createProductDb.setReservedStock(reservedStocks);
                    createProductDb.setSuccess(true);
                    CreateProductDb createProductDb1 = productRepo.save(createProductDb);
                    Optional<CreateCategoryDb> createCategoryDb;
                    List<CreateProductDto> list ;
                    for (String category : createProductRq.getCategories()) {
                        createCategoryDb = categoryRepo.findByCategoryNameAndCreatedBy(category, jwtService.extractEmail(token.substring(7)));
                        if (createCategoryDb.isPresent()) {
                            for (CreateCategorydto categoryDb : oldCategories) {
                                if (!(createProductRq.getCategories().contains(categoryDb.getCategoryName()))) {
                                    productUtils.deleteProductFromCategory(categoryDb, createProductDb1);
                                }
                            }
                            list = productUtils.updateProductDetailsInCategory(createCategoryDb.get().getProducts(), createProductDb);
                            list.add(mapper.convertValue(createProductDb1, new TypeReference<>() {
                            }));
                            CreateCategoryDb categoryDb = createCategoryDb.get();
                            categoryDb.setProducts(list);
                            categoryRepo.save(categoryDb);
                        } else {
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
        }else {
            throw new CommonException("un authorized");
        }
    }


    @Override
    public CreateCategorydto createCategory(@Valid CreateCategoryRq createCategory,String token) throws CategoryAlreadyExists {
        if (createCategory.categoryName==null){
            throw new CategoryAlreadyExists("CategoryName must Not be Null");
        }
        if (Boolean.TRUE.equals(validateToken(token))) {
            ProductUtils productUtils = new ProductUtils(productRepo, categoryRepo, mapper);
            CreateCategoryDb createCategoryDb = new CreateCategoryDb();
            createCategoryDb.setCategoryName(createCategory.categoryName);
            createCategoryDb.setCategoryImageUrl(createCategory.categoryImage);
            createCategoryDb.setCategoryCode(generateCategoryCode(createCategory.categoryName));
            createCategoryDb.setId(UUID.randomUUID().toString().split("-")[0]);
            createCategoryDb.setCreatedBy(jwtService.extractEmail(token.substring(7)));
            createCategoryDb.setCreatedAt(Calendar.getInstance().getTime());
//        createCategoryDb.setProducts(productUtils.mapProducts(createCategory.getCategoryName()));
            Optional<CreateCategoryDb> lis = categoryRepo.findByCategoryNameAndCreatedBy(createCategory.categoryName,jwtService.extractEmail(token.substring(7)));

            CreateCategorydto createCategorydto;
            if (lis.isEmpty()) {
                CreateCategoryDb categoryDb = categoryRepo.save(createCategoryDb);
                createCategorydto = mapper.convertValue(categoryDb, new TypeReference<>() {
                });

            } else {
                throw new CategoryAlreadyExists("category name is exists with categoryName " + createCategoryDb.getCategoryName());
            }
            return createCategorydto;
        }else {
            throw  new CommonException("un authorized");
        }
    }

    private Boolean validateToken(String token) {
       String userName=jwtService.extractEmail(token.substring(7));
        var userDetails=memberRepo.findByEmail(userName);
       if (userDetails.get().getRole().equals(Role.ADMIN)||userDetails.get().getRole().equals(Role.SUPERADMIN)){
           return true;
        }
        return false;
    }

    public BaseResponse<?> getAllCategories(String token){
        Optional<List<CreateCategoryDb>> categoryDb=categoryRepo.findByCreatedBy(jwtService.extractEmail(token.substring(7)));
        if (categoryDb.isEmpty()||categoryDb.get().size()==0){
            return new BaseResponse<>(HttpStatus.OK.value(),"success",true,null,"EMPTY_VALUE");
        }
        List<CategoryforProductDto> categoryDtos = categoryDb.get().stream()
                .map(this::convertToCategoryforProductDto)
                .collect(Collectors.toList());

        return new BaseResponse<>(HttpStatus.OK.value(), "success", true, null, categoryDtos);

    }
    private CategoryforProductDto convertToCategoryforProductDto(CreateCategoryDb categoryDb) {
        CategoryforProductDto categoryDto = new CategoryforProductDto();
        categoryDto.setId(categoryDb.getId());
        categoryDto.setCategoryCode(categoryDb.getCategoryCode());
        categoryDto.setCategoryName(categoryDb.getCategoryName());
        categoryDto.setCategoryImageUrl(categoryDb.getCategoryImageUrl());

        return categoryDto;
    }
    @Override
    public CreateCategorydto getSpecficCategory(String categoryName,String token) throws NoSuchCategoryFound {
        if (categoryName!=null) {
            Optional<CreateCategoryDb> createCategoryDb=categoryRepo.findByCategoryNameAndCreatedBy(categoryName, jwtService.extractEmail(token.substring(7)));
//            Optional<CreateCategoryDb> createCategoryDb=categoryRepo.findBycategoryName(categoryName);
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
    public Boolean deleteCategory(String categoryName,String token) throws CommonException, NoSuchCategoryFound {
        if (categoryName==null||categoryName.equals("null"))
            throw new NoSuchCategoryFound("The Category Name must not be null");
        if (validateToken(token)) {
            Optional<CreateCategoryDb> optionalCreateCategoryDb = categoryRepo.findByCategoryNameAndCreatedBy(categoryName,jwtService.extractEmail(token.substring(7)));
            if (optionalCreateCategoryDb.isEmpty())
                throw new CommonException("Category Name is invalid");
            else {
                ProductUtils productUtils = new ProductUtils(productRepo, categoryRepo, mapper);
                productUtils.categoriesFromAllProduct(optionalCreateCategoryDb.get(),jwtService.extractEmail(token.substring(7)));
                categoryRepo.delete(optionalCreateCategoryDb.get());
            }
            Optional<CreateCategoryDb> optionalCreateCategoryDb1 = categoryRepo.findByCategoryNameAndCreatedBy(categoryName,jwtService.extractEmail(token.substring(7)));
            if (optionalCreateCategoryDb1.isPresent())
                throw new CommonException("Category " + categoryName + " is not deleted");
            else
                return true;
        }else {
            throw new CommonException("un authorized");
        }
    }

    @Override
    public CreateCategorydto updateCategory(@NotNull CreateCategoryRq updateCategory,String token) throws CommonException, IOException {
        if (Boolean.TRUE.equals(validateToken(token))) {
        if (updateCategory.categoryName==null||updateCategory.updatedCategoryName==null||updateCategory.updatedCategoryName.length()<3)
            throw new CommonException("CategoryName or Image is null");
        else {
            Optional<CreateCategoryDb> optionalCreateCategoryDb = categoryRepo.findByCategoryNameAndCreatedBy(updateCategory.categoryName,jwtService.extractEmail(token.substring(7)));
            if (optionalCreateCategoryDb.isPresent()) {
                CreateCategoryDb createCategoryDb = optionalCreateCategoryDb.get();
                if (updateCategory.categoryImage == null)
                    createCategoryDb.setCategoryImageUrl(createCategoryDb.getCategoryImageUrl());
                else
                    createCategoryDb.setCategoryImageUrl(updateCategory.categoryImage);
                createCategoryDb.setId(createCategoryDb.getId());
                createCategoryDb.setCategoryName(updateCategory.updatedCategoryName);
                categoryRepo.save(createCategoryDb);
                return mapper.convertValue(createCategoryDb, new TypeReference<CreateCategorydto>() {
                });
            } else {
                throw new CommonException("category name doesn't exists");
            }
        }
        }else {
            throw new CommonException("un authorized");
        }
    }
    public static String generateCategoryCode(String productName){
         random  = new Random();
        if (productName!=null)
            return productName.substring(0,3).toUpperCase(Locale.ROOT)+"-"+"60"+random.nextInt(1000);
        else
            return null;
    }
}