package com.mini.ecommerce.cart.dto.response.cart;

import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import com.mini.ecommerce.cart.dto.response.product.ProductForCategory;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Builder
@Data
public class AddToCart {
    private String cartId;
    private List<ProductForCategory> products;
    private Double totalPrice;
    private Integer quantity;
    private Date createdAt;
}
