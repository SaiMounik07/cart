package com.mini.ecommerce.cart.services.impl.mailImpl;

import com.mini.ecommerce.cart.dto.response.mail.MailBody;
import com.mini.ecommerce.cart.models.entities.MailDB;
import com.mini.ecommerce.cart.repositories.mail.MailRepo;
import com.mini.ecommerce.cart.services.Mail;
import com.mini.ecommerce.cart.services.impl.memberImpl.MemberUtils;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class MailImpl implements Mail {

    private static final Logger log = LoggerFactory.getLogger(MailImpl.class);
    @Autowired
    MailRepo mailRepo;
    MailDB mailDB;

    @Override
    public Boolean sendMail(MailBody mailBody) {
        try {
            log.info("started to send mail");
            final String username = "asai@gmail.com";
            final String password = "";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.debug", "true");
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            log.info("mail instance is created");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailBody.getReciepient()));
            message.setSubject("verification code");
            message.setText(mailBody.getBody()+"\n"+mailBody.getCode().get(mailBody.getReciepient()));
            Transport.send(message);
            log.info("mail triggered with {}",mailBody.getBody());
            return true;
        }
        catch (Exception exception){
            log.info("failed");
            return false;
        }

    }

    @Override
    public void saveOTP(MailBody mailBody,String email) {
        Optional<MailDB> mailDB1=mailRepo.findByEmail(email);
        if (mailDB1.isEmpty()) {
            mailDB = new MailDB();
            mailDB.setCode(mailBody.getCode().get(mailBody.getReciepient()));
            mailDB.setEmail(mailBody.getReciepient());
            mailDB.setCurrentTime(System.currentTimeMillis());
            mailDB.setExpirationTime(TimeUnit.MINUTES.toMillis(3) + mailDB.getCurrentTime());
        }else {
            mailDB1.get().setCode(mailBody.getCode().get(mailBody.getReciepient()));
            mailDB1.get().setEmail(mailBody.getReciepient());
            mailDB1.get().setCurrentTime(System.currentTimeMillis());
            mailDB1.get().setExpirationTime(TimeUnit.MINUTES.toMillis(3) + mailDB1.get().getCurrentTime());
            mailDB=mailDB1.get();
        }
        mailRepo.save(mailDB);
    }
}
