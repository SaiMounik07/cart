package com.mini.ecommerce.cart.controllers;

import com.mini.ecommerce.cart.dto.request.AddMemberRequest;
import com.mini.ecommerce.cart.dto.response.member.AddMemberDto;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.BaseResponse;
import com.mini.ecommerce.cart.services.Member;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping(value = "/member")
public class MemberController {
    @Autowired
    Member member;

    @PostMapping("/addMember")
    public BaseResponse<AddMemberDto> addMember(@RequestBody AddMemberRequest addMemberRequest) throws CommonException {
        return member.addMember(addMemberRequest);
    }

    @GetMapping("/verify/{email}/{OTP}")
    public BaseResponse<String> verifyOtp(@PathVariable String email,@PathVariable String OTP) throws CommonException {
        return member.verifyOTP(email,OTP);
    }

    @GetMapping("/resend/{email}")
    public BaseResponse<Boolean> resend(@PathVariable String email){
        return  member.resendOTP(email);
    }

}
