package com.mini.ecommerce.cart.dto.response.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class CategoryforProductDto{
    @Id
    public String id;
    public String categoryCode;
    public String categoryName;
    public String categoryImageUrl;
}
