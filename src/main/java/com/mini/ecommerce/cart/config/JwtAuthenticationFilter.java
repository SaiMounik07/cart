package com.mini.ecommerce.cart.config;

import com.mini.ecommerce.cart.exceptionhandler.CommonException;
import com.mini.ecommerce.cart.repositories.member.MemberRepo;
import com.mini.ecommerce.cart.repositories.token.JwtTokenRepo;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenRepo jwtTokenRepo;


    @Override
    public void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
    final String authHeader=request.getHeader("Authorization");
        final String email;
    if (authHeader==null||!authHeader.startsWith("Bearer ")){
        filterChain.doFilter(request,response);
        return;
    }
    final String jwtToken=authHeader.substring(7);
    try {
        email = jwtService.extractEmail(jwtToken);
    }catch (ExpiredJwtException exception){
      throw new CommonException("token expired");
    }
    if (email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
        UserDetails userDetails=userDetailsService.loadUserByUsername(email);
         var isTokenValid = jwtTokenRepo.findByToken(jwtToken)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtService.isTokenValid(jwtToken,userDetails)&&isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
        filterChain.doFilter(request,response);

    }
}
