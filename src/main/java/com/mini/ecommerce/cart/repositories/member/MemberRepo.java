package com.mini.ecommerce.cart.repositories.member;

import com.mini.ecommerce.cart.models.entities.MemberDB;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepo extends CrudRepository<MemberDB,Integer> {
    @Query(value = "SELECT * FROM member_details where mobile_number = ?1",nativeQuery = true)
    Optional<MemberDB> findByMobileNumber(String mobileNumber);

    @Query(value = "SELECT * FROM member_details where email = ?1",nativeQuery = true)
    Optional<MemberDB> findByEmail(String email);
}
