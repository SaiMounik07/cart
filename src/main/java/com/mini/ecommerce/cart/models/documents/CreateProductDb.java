package com.mini.ecommerce.cart.models.documents;

import com.mini.ecommerce.cart.dto.response.product.CreateCategorydto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection="productDetails")
@Data
public class CreateProductDb {
    @Id
    public String id;
    private Boolean success;
    private String productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private Double productOfferPrice;
    private Double productListPrice;
    private List<CreateCategorydto> categories;
    private Integer initialStocks;
    private Integer reservedStock;
    private Integer totalStockReplenished;
    private Boolean isActive;
    private Boolean isOutOfStock;
    private String createdBy;
    private Date createdAt;
}
