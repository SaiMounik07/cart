package com.mini.ecommerce.cart.dto.response;

import com.mini.ecommerce.cart.dto.request.CreateProductRq;
import com.mini.ecommerce.cart.models.documents.CreateProductDb;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class CreateProductDto extends ProductForCategory {
    private List<CategoryforProductDto> categories;
//    private Integer initialStocks;
//    private Integer reservedStock;
//    private Integer totalStockReplenished;
}
