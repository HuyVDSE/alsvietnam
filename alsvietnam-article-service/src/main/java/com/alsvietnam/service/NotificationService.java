package com.alsvietnam.service;

import com.alsvietnam.models.dtos.notification.NotificationDto;
import com.alsvietnam.models.search.ParameterSearchNotification;
import com.alsvietnam.models.wrapper.ListWrapper;

import java.util.List;

/**
 * Duc_Huy
 * Date: 10/6/2022
 * Time: 10:19 PM
 */

public interface NotificationService {

    List<NotificationDto> getNotificationByUserId(String userId);

    ListWrapper<NotificationDto> searchNotification(ParameterSearchNotification parameterSearch);

    void createNotification(String userId, String title, String content);

    void updateStatusNotification(String notificationId);

    void notifyNewRequestVolunteer();
}
