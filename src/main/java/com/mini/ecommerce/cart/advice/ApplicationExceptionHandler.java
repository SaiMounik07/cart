package com.mini.ecommerce.cart.advice;

import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.exceptionhandler.CommonOkException;
import com.mini.ecommerce.cart.exceptionhandler.NoSuchCategoryFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;



@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CategoryAlreadyExists.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String,String> handleCategoryException(CategoryAlreadyExists categoryAlreadyExists){
        HashMap<String,String> category=new HashMap<>();
        category.put("errorMessage",categoryAlreadyExists.getMessage());
        return category;
    }

    @ExceptionHandler(NoSuchCategoryFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String,Object> noCategoryFound(NoSuchCategoryFound noSuchCategoryFound){
        HashMap<String,Object> category=new HashMap<>();
        category.put("success",false);
        category.put("errorMessage",noSuchCategoryFound.getMessage());
        return category;
    }
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String,Object> commonExceptionHandler(CommonException commonException){
        HashMap<String,Object> commonExceptions=new HashMap<>();
        commonExceptions.put("success",false);
        commonExceptions.put("errorMessage",commonException.getMessage());
        return commonExceptions;
    }

    @ExceptionHandler(CommonOkException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public HashMap<String,Object> commonOkException(CommonOkException commonOkException){
        HashMap<String,Object> commonExceptions=new HashMap<>();
        commonExceptions.put("success",false);
        commonExceptions.put("errorMessage",commonOkException.getMessage());
        return  commonExceptions;
    }
}
