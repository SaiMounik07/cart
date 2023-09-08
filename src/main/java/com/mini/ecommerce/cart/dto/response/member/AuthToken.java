package com.mini.ecommerce.cart.dto.response.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class AuthToken {
    private String token;
}
