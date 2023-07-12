package com.mini.ecommerce.cart.advice;

import com.mini.ecommerce.cart.exceptionhandler.CategoryAlreadyExists;
import org.springframework.beans.factory.annotation.Autowired;
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
    public HashMap<String,String> handleCategoryException(CategoryAlreadyExists cat){
        HashMap<String,String> category=new HashMap<>();
        category.put("errorMessage",cat.getMessage());
        return category;
    }
}
