package com.champlain.ateliermecaniquews.authenticationsubdomain.dataLayer.repositories;

import com.champlain.ateliermecaniquews.authenticationsubdomain.dataLayer.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
