package com.alsvietnam.service.impl;

import com.alsvietnam.entities.Notification;
import com.alsvietnam.entities.User;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.notification.NotificationDto;
import com.alsvietnam.models.search.ParameterSearchNotification;
import com.alsvietnam.models.search.ParameterSearchUser;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.NotificationService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.EnumConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Duc_Huy
 * Date: 10/6/2022
 * Time: 10:28 PM
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl extends BaseService implements NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public List<NotificationDto> getNotificationByUserId(String userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notificationConverter.toDtos(notifications);
    }

    @Override
    public ListWrapper<NotificationDto> searchNotification(ParameterSearchNotification parameterSearch) {
        ListWrapper<Notification> wrapper = notificationRepository.searchNotification(parameterSearch);
        List<NotificationDto> notificationDtos = notificationConverter.toDtos(wrapper.getData());

        return ListWrapper.<NotificationDto>builder()
                .total(wrapper.getTotal())
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .totalPage(wrapper.getTotalPage())
                .data(notificationDtos)
                .build();
    }

    @Override
    public void createNotification(String userId, String title, String content) {
        log.info("Create notification");
        NotificationDto dto = new NotificationDto(title, content, userId);
        Notification notification = notificationConverter.fromCreateDto(dto);
        notificationRepository.save(notification);
        dto = notificationConverter.toDto(notification);

        // push notification to socket
        simpMessagingTemplate.convertAndSend("/specific/" + userId, dto);

        logDataService.create(notification.getId(), EnumConst.LogTypeEnum.NOTIFICATION.name(),
                "Create notification " + notification.getId() + " success");
    }

    @Override
    public void updateStatusNotification(String notificationId) {
        log.info("Update top organization support: {}", notificationId);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ServiceException("Notification not found"));

        notification.setStatus(true);
        notificationRepository.save(notification);

        logDataService.create(notification.getId(), EnumConst.LogTypeEnum.NOTIFICATION.name(),
                "Update status notification " + notification.getId() + " success");
    }

    @Async
    @Override
    public void notifyNewRequestVolunteer() {
        ParameterSearchUser searchUser = new ParameterSearchUser();
        searchUser.setRoles(List.of(EnumConst.RoleEnum.ADMIN));
        List<User> users = userRepository.searchUsers(searchUser).getData();
        for (User user : users) {
            createNotification(user.getId(), "New Request Volunteer", "You have new request volunteer, go to request member list to check");
        }
    }
}
