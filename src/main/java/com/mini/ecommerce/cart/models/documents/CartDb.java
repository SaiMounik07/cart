package com.mini.ecommerce.cart.models.documents;

import com.mini.ecommerce.cart.dto.response.product.CreateProductDto;
import com.mini.ecommerce.cart.dto.response.product.ProductForCategory;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection="pending_cart")
@Data
public class CartDb {
    @Id
    private String cartId;
    private List<ProductForCategory> products;
    private Double totalPrice;
    private Integer quantity;
    private Date createdAt;
}
