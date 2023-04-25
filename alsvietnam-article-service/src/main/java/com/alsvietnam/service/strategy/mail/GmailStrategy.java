package com.alsvietnam.service.strategy.mail;

import com.alsvietnam.properties.MailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Duc_Huy
 * Date: 9/23/2022
 * Time: 12:24 AM
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class GmailStrategy implements MailStrategy {

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private final MailProperties mailProperties;

    @Override
    public void sendMail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.info("Send email [multipart '{}' and html '{}'] to '{}' with subject '{}'", isMultipart, isHtml, to, subject);

        // Prepare msg using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(mailProperties.getAddress());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.info("Send email to User '{}'", to);
        } catch (Exception e) {
            log.warn("Email couldn't be sent to user '{}': {}", to, e.getMessage());
        }
    }

    @Override
    public void sendMailFromTemplate(String to, Map<String, Object> mapValue, String templateName, String title) {
        Context context = new Context();
        context.setVariables(mapValue);
        String content = templateEngine.process(templateName, context);
        sendMail(to, title, content, false, true);
    }
}
