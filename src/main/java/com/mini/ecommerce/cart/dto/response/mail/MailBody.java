package com.mini.ecommerce.cart.dto.response.mail;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class MailBody {
    private String reciepient;
    private String subject;
    private String body;
    private LinkedHashMap<String,String> code;
}
