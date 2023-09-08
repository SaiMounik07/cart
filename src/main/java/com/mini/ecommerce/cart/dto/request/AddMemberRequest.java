package com.mini.ecommerce.cart.dto.request;

import com.mini.ecommerce.cart.dto.response.member.Role;
import com.mini.ecommerce.cart.models.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

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
    private Role role;
    private String dob;

}
