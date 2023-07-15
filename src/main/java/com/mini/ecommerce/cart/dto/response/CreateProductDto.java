package com.mini.ecommerce.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class CreateProductDto {
    @Id
    public String id;
    private String productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private Double productOfferPrice;
    private Double productListPrice;
    private List<CreateCategorydto> categories;
//    private Integer initialStocks;
//    private Integer reservedStock;
//    private Integer totalStockReplenished;
    private Boolean isActive;
    private Boolean isOutOfStock;

}
