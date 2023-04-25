package com.alsvietnam.service.base;

import com.alsvietnam.converter.*;
import com.alsvietnam.entities.User;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.task.TimeRange;
import com.alsvietnam.repository.*;
import com.alsvietnam.service.*;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.Extensions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 8:20 PM
 */

public abstract class BaseService {

    // Repository

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected LogDataRepository logDataRepository;

    @Autowired
    protected TopicRepository topicRepository;

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected PaymentMethodRepository paymentMethodRepository;

    @Autowired
    protected PaymentAccountRepository paymentAccountRepository;

    @Autowired
    protected DonationRepository donationRepository;

    @Autowired
    protected ArticleFileRepository articleFileRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TeamRepository teamRepository;

    @Autowired
    protected ArticleContentRepository articleContentRepository;

    @Autowired
    protected ArticleMediaRepository articleMediaRepository;

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected NotificationRepository notificationRepository;

    @Autowired
    protected VerificationEmailRepository verificationEmailRepository;

    @Autowired
    protected DonationCampaignRepository donationCampaignRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected HonoredTableRepository honoredTableRepository;

    @Autowired
    protected HonoredUserRepository honoredUserRepository;

    @Autowired
    protected StoryRepository storyRepository;

    @Autowired
    protected UserTaskRepository userTaskRepository;

    @Autowired
    protected CategoryFaqRepository categoryFaqRepository;

    @Autowired
    protected FaqRepository faqRepository;

    @Autowired
    protected TopOrganizationSupportRepository topOrganizationSupportRepository;

    @Autowired
    protected FileExternalRepository fileExternalRepository;

    // Service

    @Autowired
    protected LogDataService logDataService;

    @Autowired
    protected FileService fileService;

    @Autowired
    protected ArticleService articleService;

    @Autowired
    protected FileStorageService fileStorageService;

    protected UserService userService;

    // Converter

    @Autowired
    protected TopicConverter topicConverter;

    @Autowired
    protected ArticleConverter articleConverter;

    @Autowired
    protected DonationConverter donationConverter;

	@Autowired
    protected UserConverter userConverter;

    @Autowired
    protected TaskConverter taskConverter;

    @Autowired
    protected NotificationConverter notificationConverter;

    @Autowired
    protected VerificationEmailConverter verificationEmailConverter;

    @Autowired
    protected CommentConverter commentConverter;

    @Autowired
    protected CampaignConverter campaignConverter;

    @Autowired
    protected StoryConverter storyConverter;

    @Autowired
    protected HonoredTableConverter honoredTableConverter;

    @Autowired
    protected TopOrganizationSupportConverter topOrganizationSupportConverter;

    // getter

    protected User getCurrentUser() {
        return userService.getUserLogin();
    }

    protected String getCurrentUsername() {
        return userService.getUsernameLogin();
    }

    // setter

    // use setter injection to avoid circular dependency
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // other
    protected <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    protected TimeRange validateTimeRange(String startDate, String endDate) {
        Date begin;
        if (Extensions.isBlankOrNull(startDate)) {
            begin = DateUtil.customFromDate(new Date());
        } else {
            begin = DateUtil.formatDateString(startDate, DateUtil.TYPE_FORMAT_1);
            begin = DateUtil.customFromDate(begin);
            if (begin.before(DateUtil.customFromDate(new Date()))) {
                throw new ServiceException("Start date must after or equal current date");
            }
        }

        Date end = null;
        if (!Extensions.isBlankOrNull(endDate)) {
            end = DateUtil.formatDateString(endDate, DateUtil.TYPE_FORMAT_1);
            end = DateUtil.customToDate(end);
            if (begin.after(end) || begin.equals(end)) {
                throw new ServiceException("End date must after Begin date");
            }
        }
        return new TimeRange(begin, end);
    }
}
