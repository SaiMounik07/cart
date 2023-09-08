package com.mini.ecommerce.cart.services.impl.auth;

import com.mini.ecommerce.cart.config.JwtService;
import com.mini.ecommerce.cart.dto.request.MemberLoginRequest;
import com.mini.ecommerce.cart.dto.response.member.AuthToken;
import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.models.BaseResponse;
import com.mini.ecommerce.cart.models.entities.MemberDB;
import com.mini.ecommerce.cart.models.entities.Token;
import com.mini.ecommerce.cart.repositories.member.MemberRepo;
import com.mini.ecommerce.cart.repositories.token.JwtTokenRepo;
import com.mini.ecommerce.cart.services.Auth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthImpl implements Auth, LogoutHandler {
    @Autowired
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    Token token;
    @Autowired
    private JwtTokenRepo jwtTokenRepo;
    @Override
    public BaseResponse<AuthToken> login(MemberLoginRequest memberLoginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        memberLoginRequest.getEmail(),
                        memberLoginRequest.getPassword()
                )
        );
        var user=memberRepo.findByEmail(memberLoginRequest.getEmail())
                .orElseThrow();

        if (user.getIsVerified()) {
            var jwtToken = jwtService.generateJwtToken(user);
            token.setUser(user);
            token.setToken(jwtToken);
            token.setRevoked(false);
            token.setExpired(false);
            jwtTokenRepo.save(token);
            return new BaseResponse<>(HttpStatus.OK.value(), "success", true, null, AuthToken.builder()
                    .token(jwtToken)
                    .build());
        }
        else {
            throw new CommonException("user not verified");
        }
    }
    private void revokeAllUserTokens(MemberDB user) {
        var validUserTokens = jwtTokenRepo.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtTokenRepo.saveAll(validUserTokens);
    }
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = jwtTokenRepo.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            jwtTokenRepo.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }


    }

