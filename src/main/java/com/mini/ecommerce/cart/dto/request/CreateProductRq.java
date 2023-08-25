package com.mini.ecommerce.cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRq {
    @NotNull(message = "productName must Not be null")
    private String productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private Double productOfferPrice;
    private Double productListPrice;
    private List<String> categories;
    private Integer initialStocks;
    private Boolean isActive;
}
