package com.mini.ecommerce.cart.dto.request;

import com.mini.ecommerce.cart.services.impl.memberImpl.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberRequest {
    private String name;
    private Gender gender;
    private Integer age;
    @Email
    private String email;
    private String password;
    private String confirmPassword;
    private String mobileNumber;
    private String dob;

}
