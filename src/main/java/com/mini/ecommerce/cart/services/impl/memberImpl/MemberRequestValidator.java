package com.mini.ecommerce.cart.services.impl.memberImpl;

import com.mini.ecommerce.cart.dto.request.AddMemberRequest;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;

public class MemberRequestValidator {
    public Boolean addMemberRequest(AddMemberRequest addRequest) throws CommonException {
        if (!(addRequest.getEmail()==null || addRequest.getMobileNumber()==null || addRequest.getPassword()==null||addRequest.getConfirmPassword()==null)&&(addRequest.getPassword().equals(addRequest.getConfirmPassword()))){
            return true;
        }else {
            throw new CommonException("some fields are null");
        }
    }

}
