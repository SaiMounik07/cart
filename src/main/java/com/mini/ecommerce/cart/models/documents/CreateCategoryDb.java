package com.mini.ecommerce.cart.models.documents;

import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection="category")
@Data
public class CreateCategoryDb {
    @Id
    public String id;
    public String categoryCode;
    public String categoryName;
    public String categoryImageUrl;
    public List<CreateProductDto> products;
}
