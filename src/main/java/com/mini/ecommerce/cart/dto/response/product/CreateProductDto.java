package com.mini.ecommerce.cart.dto.response.product;

import lombok.Data;

import java.util.List;

@Data
public class CreateProductDto extends ProductForCategory {
    private List<CategoryforProductDto> categories;
//    private Integer initialStocks;
//    private Integer reservedStock;
//    private Integer totalStockReplenished;
}
