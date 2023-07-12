package com.mini.ecommerce.cart.dto.response;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class CreateProductdto {
    @Id
    public int id;
    public String categoryCode;
    public String categortName;
    public String categoryImageUrl;
}
