package com.mini.ecommerce.cart.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class CreateCategorydto extends CategoryforProductDto {
    @NotNull
    public List<ProductForCategory> products;


}
