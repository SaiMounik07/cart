package com.mini.ecommerce.cart.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OtpRequest {
    private String email;
    private String otp;
}
