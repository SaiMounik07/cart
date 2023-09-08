package com.mini.ecommerce.cart.controllers;

import com.mini.ecommerce.cart.dto.request.AddMemberRequest;
import com.mini.ecommerce.cart.dto.request.MemberLoginRequest;
import com.mini.ecommerce.cart.dto.request.OtpRequest;
import com.mini.ecommerce.cart.dto.response.member.AddMemberDto;
import com.mini.ecommerce.cart.dto.response.member.AuthToken;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.BaseResponse;
import com.mini.ecommerce.cart.services.Auth;
import com.mini.ecommerce.cart.services.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping(value = "/member")
public class MemberController {
    @Autowired
    Member member;
    @Autowired
    Auth auth;

    @PostMapping("/addMember")
    public BaseResponse<AddMemberDto> addMember(@RequestBody AddMemberRequest addMemberRequest) throws CommonException {
        return member.addMember(addMemberRequest);
    }

    @PostMapping("/verify")
    public BaseResponse<String> verifyOtp(@RequestBody OtpRequest otpRequest) throws CommonException {
        return member.verifyOTP(otpRequest);
    }

    @GetMapping("/resend")
    public BaseResponse<Boolean> resend(@RequestHeader("email") String email){
        return  member.resendOTP(email);
    }

    /**
     *
     * @param memberLoginRequest
     * @return AuthToken
     */
    @PostMapping("/login")
    public BaseResponse<AuthToken> login(@RequestBody MemberLoginRequest memberLoginRequest){
        return auth.login(memberLoginRequest);
    }

}
