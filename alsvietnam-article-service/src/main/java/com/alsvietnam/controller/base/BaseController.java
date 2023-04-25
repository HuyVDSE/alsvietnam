package com.alsvietnam.controller.base;

import com.alsvietnam.converter.DonationConverter;
import com.alsvietnam.converter.TeamConverter;
import com.alsvietnam.repository.*;
import com.alsvietnam.service.*;
import com.alsvietnam.service.strategy.mail.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 9:17 PM
 */

public abstract class BaseController {

    // variable

    @Autowired
    protected ObjectMapper objectMapper;

    // Service

    @Autowired
    protected RoleService roleService;

    @Autowired
    protected TopicService topicService;

    @Autowired
    protected ArticleService articleService;

    @Autowired
    protected PaymentMethodService paymentMethodService;

    @Autowired
    protected DonationService donationService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected TeamService teamService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected MailService mailService;

    @Autowired
    protected FileService fileService;

    @Autowired
    protected FileStorageService fileStorageService;

    @Autowired
    protected DonationCampaignService donationCampaignService;

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected StoryService storyService;

    @Autowired
    protected HonoredTableService honoredTableService;

    @Autowired
    protected FaqService faqService;

    @Autowired
    protected TopOrganizationSupportService topOrganizationSupportService;

    @Autowired
    protected NotificationService notificationService;

    // Repository

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected TopicRepository topicRepository;

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected DonationRepository donationRepository;

    @Autowired
    protected TeamRepository teamRepository;

    // Converter

    @Autowired
    protected TeamConverter teamConverter;

    @Autowired
    protected DonationConverter donationConverter;
}
