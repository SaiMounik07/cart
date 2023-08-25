package com.mini.ecommerce.cart.dto.response.member;

import lombok.Data;

import java.util.Date;

@Data
public class AddMemberDto {
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String mobileNumber;
    private String dob;
    private Boolean isActive;
    private Date createdDate;
}
