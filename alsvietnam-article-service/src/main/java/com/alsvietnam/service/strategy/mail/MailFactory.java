package com.alsvietnam.service.strategy.mail;

import com.alsvietnam.properties.MailProperties;
import com.alsvietnam.utils.EnumConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Duc_Huy
 * Date: 9/23/2022
 * Time: 10:56 AM
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class MailFactory {

    private final MailProperties mailProperties;

    private final GmailStrategy gmailStrategy;

    private final SendGridMailStrategy sendGridMailStrategy;

    public MailStrategy createStrategy() {
        log.info("Active mail profiles '{}'", mailProperties.getActiveProfile());

        // check if Active profiles contains "local" or "test"
        String activeProfile = mailProperties.getActiveProfile();
        if (activeProfile.equals(EnumConst.MailProfileEnum.GMAIL.name())) {
            return this.gmailStrategy;
        }
        if (activeProfile.equals(EnumConst.MailProfileEnum.SEND_GRID_MAIL.name())) {
            return this.sendGridMailStrategy;
        }
        return this.gmailStrategy;
    }
}
