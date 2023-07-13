package com.mini.ecommerce.cart.dto.request;

import com.mini.ecommerce.cart.dto.response.CreateCategorydto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRq {
    @NotNull(message = "productName must Not be null")
    private String productName;
    private String productDescription;
    private String productImage;
    private Double productOfferPrice;
    private Double productListPrice;
    private List<String> categories;
    private Integer intialStocks;

}
