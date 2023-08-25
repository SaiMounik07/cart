package com.mini.ecommerce.cart.dto.response.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateCategorydto extends CategoryforProductDto {
    @NotNull
    public List<ProductForCategory> products;


}
