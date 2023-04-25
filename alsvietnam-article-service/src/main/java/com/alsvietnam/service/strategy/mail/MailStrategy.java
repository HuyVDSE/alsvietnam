package com.alsvietnam.service.strategy.mail;

import java.util.Map;

/**
 * Duc_Huy
 * Date: 8/22/2022
 * Time: 12:08 AM
 */
public interface MailStrategy {

    void sendMail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    void sendMailFromTemplate(String to, Map<String, Object> mapValue, String templateName, String titleKey);

}
