package com.mini.ecommerce.cart.services;

import com.mini.ecommerce.cart.dto.request.MemberLoginRequest;
import com.mini.ecommerce.cart.dto.response.member.AuthToken;
import com.mini.ecommerce.cart.models.BaseResponse;
import org.springframework.stereotype.Service;

@Service
public interface Auth {

     BaseResponse<AuthToken> login(MemberLoginRequest memberLoginRequest);

}
