package com.alsvietnam.service.strategy.mail;

import com.alsvietnam.converter.VerificationEmailConverter;
import com.alsvietnam.entities.Task;
import com.alsvietnam.entities.User;
import com.alsvietnam.properties.MailProperties;
import com.alsvietnam.repository.VerificationEmailRepository;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Duc_Huy
 * Date: 9/23/2022
 * Time: 11:23 AM
 */

@Service
@Slf4j
public class MailService {

    private final MailStrategy mailStrategy;

    private final MailProperties mailProperties;

    private final VerificationEmailConverter verificationEmailConverter;

    private final VerificationEmailRepository verificationEmailRepository;

    @Value("${domain.blog}")
    private String domainBlog;

    @Value("${domain.portal}")
    private String domainPortal;

    @Autowired
    public MailService(MailFactory mailFactory,
                       MailProperties mailProperties,
                       VerificationEmailRepository verificationEmailRepository,
                       VerificationEmailConverter verificationEmailConverter) {
        this.mailStrategy = mailFactory.createStrategy();
        this.mailProperties = mailProperties;
        this.verificationEmailRepository = verificationEmailRepository;
        this.verificationEmailConverter = verificationEmailConverter;
    }

    @Async
    public void sendAssignTaskEmail(String email, String manager, String member, Task task) {
        if (Extensions.isBlankOrNull(email)) {
            return;
        }
        Map<String, Object> mapValue = new HashMap<>();
        mapValue.put("manager", manager);
        mapValue.put("member", member);
        mapValue.put("task", task);
        mapValue.put("send_member", true);
        mapValue.put("link", "#"); // update link later
        mailStrategy.sendMailFromTemplate(email, mapValue, "mail/assignTaskEmail", mailProperties.getAssignTaskTitle());
    }

    @Async
    public void sendUnAssignTaskEmail(String email, String manager, String member, Task task) {
        if (Extensions.isBlankOrNull(email)) {
            return;
        }
        Map<String, Object> mapValue = new HashMap<>();
        mapValue.put("manager", manager);
        mapValue.put("member", member);
        mapValue.put("task", task);
        mailStrategy.sendMailFromTemplate(email, mapValue, "mail/unAssignTaskEmail", mailProperties.getUnAssignTaskTitle());
    }

    @Async
    public void sendFinishTaskEmail(String email, String manager, Task task) {
        if (Extensions.isBlankOrNull(email)) {
            return;
        }
        Map<String, Object> mapValue = new HashMap<>();
        mapValue.put("manager", manager);
        mapValue.put("task", task);
        mapValue.put("link", "#");
        mailStrategy.sendMailFromTemplate(email, mapValue, "mail/finishTaskEmail", mailProperties.getFinishTaskTitle());
    }

    @Async
    public void sendAssignManageTask(String email, String managerFrom, Task task) {
        if (Extensions.isBlankOrNull(email)) {
            return;
        }
        Map<String, Object> mapValue = new HashMap<>();
        mapValue.put("manager_from", managerFrom);
        mapValue.put("task", task);
        mapValue.put("send_member", false);
        mapValue.put("link", "#"); // update link later
        mailStrategy.sendMailFromTemplate(email, mapValue, "mail/assignTaskEmail", mailProperties.getAssignManageTaskTitle());
    }

    @Async
    public void sendVerifyCodeEmail(String email, String verifyCode, EnumConst.VerifyEmailType verifyEmailType) {
        Map<String, Object> mapValue = new HashMap<>();
        mapValue.put("verifyId", verifyCode);
        mapValue.put("type", verifyEmailType.toString());
        mailStrategy.sendMailFromTemplate(email, mapValue, "mail/verifyCodeEmail", mailProperties.getVerifyCodeEmailTitle());
    }

    @Async
    public void sendMailRequestMemberResult(User user, boolean requestStatus) {
        if (user.getEmail() == null) return;
        Map<String, Object> mapValue = new HashMap<>();
        mapValue.put("request_status", requestStatus);
        mapValue.put("firstname", user.getFirstName());
        mapValue.put("username", user.getUsername());
        mapValue.put("domain_portal", domainPortal);
        mapValue.put("domain_blog", domainBlog);
        mailStrategy.sendMailFromTemplate(user.getEmail(), mapValue, "mail/requestMemberResult", mailProperties.getRequestMemberResult());
    }
}
