package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.security.annotation.IsAdmin;
import com.alsvietnam.service.SchedulerService;
import com.alsvietnam.utils.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Duc_Huy
 * Date: 11/7/2022
 * Time: 11:38 PM
 */

@IsAdmin
@RestController
@RequestMapping(Constants.SCHEDULER_SERVICE)
@RequiredArgsConstructor
@Tag(name = "Scheduler", description = "Scheduler API")
public class SchedulerController extends BaseController {

    private final SchedulerService schedulerService;

    @GetMapping("/notify-deadline-task")
    public void notifyDeadlineTask() {
        schedulerService.notifyDeadlineTask();
    }

    @GetMapping("/update-campaign-active-status")
    public void updateCampaignActiveStatus() {
        schedulerService.updateCampaignActiveStatus();
    }

    @GetMapping("/remove-request-volunteer-expired")
    public void removeRequestVolunteerExpired() {
        schedulerService.removeRequestVolunteerExpired();
    }
}
