package com.alsvietnam.repository;

import com.alsvietnam.entities.Notification;
import com.alsvietnam.models.search.ParameterSearchNotification;
import com.alsvietnam.models.wrapper.ListWrapper;

public interface NotificationRepositoryCustom {

    ListWrapper<Notification> searchNotification(ParameterSearchNotification searchParameter);
}
