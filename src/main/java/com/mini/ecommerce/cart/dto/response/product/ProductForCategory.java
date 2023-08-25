package com.mini.ecommerce.cart.dto.response.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
@Data
public class ProductForCategory {
    @Id
    public String id;
    private String productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private Double productOfferPrice;
    private Double productListPrice;
    private Boolean isActive;
    private Boolean isOutOfStock;
}
