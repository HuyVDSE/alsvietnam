package com.alsvietnam.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Duc_Huy
 * Date: 9/23/2022
 * Time: 11:12 AM
 */

@Data
@Component
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String address;

    private String activeProfile;

    private String assignTaskTitle;

    private String unAssignTaskTitle;

    private String finishTaskTitle;

    private String assignManageTaskTitle;

    private String verifyCodeEmailTitle;

    private String requestMemberResult;

}
