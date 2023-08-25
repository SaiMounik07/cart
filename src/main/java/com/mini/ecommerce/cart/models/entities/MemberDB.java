package com.mini.ecommerce.cart.models.entities;

import com.mini.ecommerce.cart.services.impl.memberImpl.Gender;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
@Entity
@Data
@Table(name = "memberDetails")
public class MemberDB {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String name;
    private Gender gender;
    private Integer age;
    private String email;
    private String password;
    private String confirmPassword;
    private String mobileNumber;
    private String dob;
    private Boolean isActive;
    private Boolean isVerified;
    private Date createdDate;

}
