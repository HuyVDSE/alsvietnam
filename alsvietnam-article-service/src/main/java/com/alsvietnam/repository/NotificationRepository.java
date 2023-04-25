package com.alsvietnam.repository;

import com.alsvietnam.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Duc_Huy
 * Date: 10/6/2022
 * Time: 10:17 PM
 */

public interface NotificationRepository extends JpaRepository<Notification, String>, NotificationRepositoryCustom {

    List<Notification> findByUserId(String userId);

}
