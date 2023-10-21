package com.mini.ecommerce.cart.outboundRequest;

import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import com.mini.ecommerce.cart.models.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public interface ProductFeign {
    @GetMapping("/getProducts")
     BaseResponse<List<CreateProductDto>> getProduct(@RequestHeader("productName") String productDetail,@RequestHeader(AUTHORIZATION) String token);
}
