package com.alsvietnam.service.strategy.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Duc_Huy
 * Date: 9/23/2022
 * Time: 11:06 AM
 */

@Service
@Slf4j
public class SendGridMailStrategy implements MailStrategy {

    @Override
    public void sendMail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {

    }

    @Override
    public void sendMailFromTemplate(String to, Map<String, Object> mapValue, String templateName, String titleKey) {

    }
}
