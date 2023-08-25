package com.mini.ecommerce.cart.services.impl.memberImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dto.request.AddMemberRequest;
import com.mini.ecommerce.cart.dto.response.mail.MailBody;
import com.mini.ecommerce.cart.dto.response.member.AddMemberDto;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.BaseResponse;
import com.mini.ecommerce.cart.models.entities.MemberDB;
import com.mini.ecommerce.cart.repositories.member.MemberRepo;
import com.mini.ecommerce.cart.services.Mail;
import com.mini.ecommerce.cart.services.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberUtils extends MemberImpl {
    private static final Logger log = LoggerFactory.getLogger(MemberUtils.class);

 @Autowired
 MemberRepo memberRepo;
 @Autowired
 ObjectMapper objectMapper;

 MailBody mailBody;
 LinkedHashMap<String,String> code=new LinkedHashMap<>();

    public MemberUtils(MemberRepo member, ObjectMapper objectMapper) {
        this.memberRepo=member;
        this.objectMapper=objectMapper;
    }
    public  boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
        Matcher matcher = pattern.matcher(password);
        log.info("is passsword matched :{}",matcher.matches());
        return matcher.matches();
    }
    public  boolean validateEmailId(String mailId){
        Pattern pattern=Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher=pattern.matcher(mailId);
        log.info("is email matched :{}",matcher.matches());

        return  matcher.matches();
    }

    public boolean validateMember(AddMemberRequest addMemberRequest) {
        Optional<MemberDB> memberDB=memberRepo.findByMobileNumber(addMemberRequest.getMobileNumber());
        Optional<MemberDB> memberDB1=memberRepo.findByEmail(addMemberRequest.getEmail());
        return memberDB.isEmpty()&&memberDB1.isEmpty();
    }

    public MailBody generateMailBody(String email) {
       mailBody=new MailBody();
       code.put(email,generateCode());
       mailBody.setBody("hi this is verification code"+"\n"+"valid for 3 minutes");
       mailBody.setReciepient(email);
       mailBody.setSubject("verification");
       mailBody.setCode(code);
       return mailBody;

    }
    public static String generateCode() {
        Random random = new Random();
        int max = 999999;
        int min = 111111;
        return String.valueOf(random.nextInt(max - min) + min);
    }

}
