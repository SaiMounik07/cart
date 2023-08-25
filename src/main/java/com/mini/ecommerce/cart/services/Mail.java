package com.mini.ecommerce.cart.services;

import com.mini.ecommerce.cart.dto.response.mail.MailBody;

public interface Mail {
    Boolean sendMail(MailBody mailBody);
    void saveOTP(MailBody mailBody,String email);
}
