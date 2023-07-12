package com.mini.ecommerce.cart.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRq {
    @NotNull(message = "categoryName must not be null")
    @NotBlank
    public String categoryName;
    public String categoryImage;

}
