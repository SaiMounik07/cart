package com.mini.ecommerce.cart.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "OtpDetails")
public class MailDB {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String email;
    private String code;
    @Column(name = "creation_time")
    private long currentTime;
    @Column(name = "expiration_time")
    private long expirationTime;
}
