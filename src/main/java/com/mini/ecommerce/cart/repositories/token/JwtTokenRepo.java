package com.mini.ecommerce.cart.repositories.token;


import com.mini.ecommerce.cart.models.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepo extends JpaRepository<Token,Integer> {

    @Query(value = "select token from Token where expired=false and revoked=false")

    List<Token> findAllValidTokenByUser(Integer id);
    Optional<Token> findByToken(String token);


}
