package com.mini.ecommerce.cart.repositories.mail;

import com.mini.ecommerce.cart.models.entities.MailDB;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MailRepo extends CrudRepository<MailDB,String> {

    Optional<MailDB> findByEmail(String s);
}
