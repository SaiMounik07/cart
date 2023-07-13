package com.mini.ecommerce.cart.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateCategoryRq {
    @NotNull
    public String categoryName;
    public String categoryImage;
    public String updatedCategoryName;
}
