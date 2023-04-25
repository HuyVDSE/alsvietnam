package com.alsvietnam.converter;

import com.alsvietnam.entities.User;
import com.alsvietnam.entities.VerificationEmail;
import com.alsvietnam.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class VerificationEmailConverter {

    public VerificationEmail fromUser(User user, String verifyCode) {
        return VerificationEmail.builder()
                .user(user)
                .verifyCode(verifyCode)
                .expiredAt(DateUtil.getDateAfterNumberTimes(new Date(), 30, DateUtil.MINUTE))
                .build();
    }
}
