package com.mini.ecommerce.cart.services;

import com.mini.ecommerce.cart.dto.request.AddMemberRequest;
import com.mini.ecommerce.cart.dto.response.member.AddMemberDto;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.BaseResponse;

public interface Member {
    BaseResponse<AddMemberDto> addMember(AddMemberRequest addMemberRequest) throws CommonException;

    BaseResponse<String> verifyOTP(String email, String OTP) throws CommonException;

    BaseResponse<Boolean> resendOTP(String email);
}
