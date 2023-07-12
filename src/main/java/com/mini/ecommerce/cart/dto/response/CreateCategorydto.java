package com.mini.ecommerce.cart.dto.response;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class CreateCategorydto {
    @Id
    public String id;
    public String categoryCode;
    public String categoryName;
    public String categoryImageUrl;
}
