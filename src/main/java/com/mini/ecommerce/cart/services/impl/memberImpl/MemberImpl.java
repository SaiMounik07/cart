package com.mini.ecommerce.cart.services.impl.memberImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerce.cart.dto.request.AddMemberRequest;
import com.mini.ecommerce.cart.dto.response.mail.MailBody;
import com.mini.ecommerce.cart.dto.response.member.AddMemberDto;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.BaseResponse;
import com.mini.ecommerce.cart.models.entities.MailDB;
import com.mini.ecommerce.cart.models.entities.MemberDB;
import com.mini.ecommerce.cart.repositories.mail.MailRepo;
import com.mini.ecommerce.cart.repositories.member.MemberRepo;
import com.mini.ecommerce.cart.services.Mail;
import com.mini.ecommerce.cart.services.Member;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class MemberImpl implements Member {
    private static final Logger log = Logger.getLogger(MemberImpl.class.getName());

    @Autowired
    MemberRepo memberRepo;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MailRepo mailRepo;
    @Autowired
    Mail mail;
    MemberUtils memberUtils;
    MailBody mailBody;
    MemberRequestValidator memberRequestValidator;
    MemberDB memberDB;
    @Override
    public BaseResponse<AddMemberDto> addMember(AddMemberRequest addMemberRequest) throws CommonException {
        MemberDB memberDB=new MemberDB();
        memberUtils=new MemberUtils(memberRepo,mapper);
        memberRequestValidator=new MemberRequestValidator();
        BeanUtils.copyProperties(addMemberRequest,memberDB);
        memberDB.setIsActive(true);
        memberDB.setIsVerified(false);
        memberDB.setCreatedDate(Calendar.getInstance().getTime());
        if (memberRequestValidator.addMemberRequest(addMemberRequest)&&memberUtils.validateEmailId(addMemberRequest.getEmail())&&memberUtils.validatePassword(addMemberRequest.getPassword())&&memberUtils.validateMember(addMemberRequest)){
           memberDB= memberRepo.save(memberDB);
           if (!memberDB.getIsVerified()&& memberDB.getEmail() != null){
               mailBody=new MailBody();
               mailBody=memberUtils.generateMailBody(addMemberRequest.getEmail());
               mail.sendMail(mailBody);
               mail.saveOTP(mailBody,addMemberRequest.getEmail());
           }

           return new BaseResponse<>(HttpStatus.OK.value(),"success",true,null,mapper.convertValue(memberDB, new TypeReference<>() {
           }));
        }
        else {
            if (memberUtils.validateMember(addMemberRequest))
                return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),"failed",false,"user already exists",null);
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),"failed",false,"Some Exception occured during recording fields",null);
        }

    }

    @Override
    public BaseResponse<String> verifyOTP(String email, String OTP) throws CommonException {
        if (email!=null&&OTP!=null){
            Optional<MailDB> mailDB=mailRepo.findByEmail(email);
            if (mailDB.isPresent()&&System.currentTimeMillis()<=mailDB.get().getExpirationTime()&& OTP.equals(mailDB.get().getCode())){
                memberDB=new MemberDB();
                Optional<MemberDB> memberDB1=memberRepo.findByEmail(email);
                memberDB=memberDB1.get();
                memberDB.setIsVerified(true);
                memberRepo.save(memberDB);
                return new BaseResponse<>(HttpStatus.OK.value(),"success",true,null,null);
            }else {
                if (mailDB.isEmpty())
                    return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),"failed",false,"invalid mail","invalid mail address or mail not registered");
                else if (System.currentTimeMillis()>mailDB.get().getExpirationTime())
                    return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),"failed",false,"time expired","otp time expired");
                else
                    return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),"failed",false,"invalid otp","entered otp is not correct");
            }
        }else {
            throw new CommonException("otp is null");
        }
    }

    @Override
    public BaseResponse<Boolean> resendOTP(String email) {
        if (email!=null) {
            memberUtils = new MemberUtils(memberRepo, mapper);
            mailBody = new MailBody();
            mailBody = memberUtils.generateMailBody(email);
            mail.sendMail(mailBody);
            mail.saveOTP(mailBody,email);
            return new BaseResponse<>(HttpStatus.OK.value(),"success",true,null,true);
        }else {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(),"failed",false,"email is null",false);
        }
    }
}
