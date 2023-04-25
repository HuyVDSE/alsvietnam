package com.alsvietnam.repository;

import com.alsvietnam.entities.User;
import com.alsvietnam.entities.VerificationEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationEmailRepository extends JpaRepository<VerificationEmail, String> {

    VerificationEmail findByVerifyCodeAndUserId(String verifyCode, String userId);

    void deleteAllByUser(User user);
}
