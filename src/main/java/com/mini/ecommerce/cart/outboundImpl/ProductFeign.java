package com.mini.ecommerce.cart.outboundImpl;

import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import com.mini.ecommerce.cart.models.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class ProductFeign implements com.mini.ecommerce.cart.outboundRequest.ProductFeign {


    @Override
    public BaseResponse<List<CreateProductDto>> getProduct(String productsku,String token) {
       try{
           log.info("product feign sucess");
       }catch (Exception exception){
           log.error("there is some exception {}",exception);
       }
        return null;
    }
}
