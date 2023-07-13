package com.mini.ecommerce.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductdto {
    @Id
    public int id;
    public String categoryCode;
    public String categortName;
    public String categoryImageUrl;
}
