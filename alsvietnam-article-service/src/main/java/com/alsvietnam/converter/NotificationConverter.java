package com.alsvietnam.converter;

import com.alsvietnam.entities.Notification;
import com.alsvietnam.models.dtos.notification.NotificationDto;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 10/6/2022
 * Time: 10:36 PM
 */

@Component
public class NotificationConverter extends BaseConverter {

    public Notification fromCreateDto(NotificationDto dto) {
        Notification notification = modelMapper.map(dto, Notification.class);
        notification.setStatus(false);
        notification.setCreatedAt(new Date());
        notification.setCreatedBy("alsvietnam.system@gmail.com");
        return notification;
    }

    public NotificationDto toDto(Notification notification) {
        return modelMapper.map(notification, NotificationDto.class);
    }

    public List<NotificationDto> toDtos(List<Notification> notifications) {
        return notifications.stream().map(this::toDto).collect(Collectors.toList());
    }
}
