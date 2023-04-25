package com.alsvietnam.models.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Duc_Huy
 * Date: 10/6/2022
 * Time: 10:25 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {

    private String id;

    private String title;

    private String content;

    private Boolean status;

    private String taskId;

    private String userId;

    public NotificationDto(String title, String content, String userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
