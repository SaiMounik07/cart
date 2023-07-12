package com.mini.ecommerce.cart.models.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Document(collection="category")
@Data
public class CreateCategoryDb {
    @Id
    public String id;
    public String categoryCode;
    public String categoryName;
    public String categoryImageUrl;
}
