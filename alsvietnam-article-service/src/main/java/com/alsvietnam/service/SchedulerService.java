package com.alsvietnam.service;

import com.alsvietnam.entities.DonationCampaign;
import com.alsvietnam.entities.Task;
import com.alsvietnam.entities.User;
import com.alsvietnam.models.search.ParameterSearchCampaign;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.search.ParameterSearchUser;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Duc_Huy
 * Date: 11/8/2022
 * Time: 8:47 PM
 */

@Service
@RequiredArgsConstructor
@ExtensionMethod(Extensions.class)
@Slf4j
public class SchedulerService extends BaseService {

    private final NotificationService notificationService;

    /**
     * <a href="https://www.freeformatter.com/cron-expression-generator-quartz.html">Scheduler Syntax</a>
     * Scheduler thông báo deadline task vào lúc 9h sáng mỗi ngày
     */
    @Scheduled(cron = "0 0 9 1/1 * ?") // scheduler run every day (rate * second * minute): đơn vị là ms
    public void notifyDeadlineTask() {
        log.info("*** Begin scheduler notify deadline task at {} ***", DateUtil.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));

        ParameterSearchTask searchParam = new ParameterSearchTask();
        Date afterOneDay = DateUtil.getDateAfterNumberTimes(new Date(), 1, DateUtil.DAILY);
        searchParam.setToEndDate(afterOneDay);
        List<String> statuses = new ArrayList<>();
        statuses.add(EnumConst.TaskStatusEnum.NEW.name());
        statuses.add(EnumConst.TaskStatusEnum.DOING.name());
        searchParam.setBuildManager(false);
        searchParam.setBuildTeam(false);
        searchParam.setStatuses(statuses);

        ListWrapper<Task> wrapper = taskRepository.searchTask(searchParam);
        List<Task> tasks = wrapper.getData();
        if (!tasks.isNullOrEmpty()) {
            for (Task task : tasks) {
                Set<User> users = task.getUsers();
                if (!users.isNullOrEmpty()) {
                    for (User user : users) {
                        notificationService.createNotification(user.getId(), "Task deadline is coming!!",
                                "End date of task [" + task.getName() + "] is " + DateUtil.convertDateToString(task.getEndDate(), "dd-MM-yyyy"));
                    }
                }
            }
        }

        log.info("*** End scheduler notify deadline task at {} ***", DateUtil.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
        log.info("==========================================================================");
    }

    @Scheduled(cron = "0 0 0 1/1 * ?") // scheduler run every day (rate * second * minute): đơn vị là ms
    public void updateCampaignActiveStatus() {
        log.info("*** Begin scheduler update donation campaign active status at {} ***",
                DateUtil.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));

        ParameterSearchCampaign searchParam = new ParameterSearchCampaign();
        searchParam.setActive(true);
        searchParam.setPageSize(9999);
        searchParam.setStartIndex(0L);
        ListWrapper<DonationCampaign> wrapper = donationCampaignRepository.searchCampaign(searchParam);
        List<DonationCampaign> donationCampaigns = wrapper.getData();
        if (!donationCampaigns.isNullOrEmpty()) {
            int size = donationCampaigns.size();
            int index = 1;
            log.info("Total campaigns: {}", size);
            Date currentEndDate = DateUtil.customToDate(new Date());
            boolean needUpdate;
            for (DonationCampaign donationCampaign : donationCampaigns) {
                log.info("index: {}/{}", index, size);
                needUpdate = false;
                if (donationCampaign.getDateEnd() != null && donationCampaign.getDateEnd().before(currentEndDate)) {
                    needUpdate = true;
                }
                if (donationCampaign.getExpectedAmount().equals(donationCampaign.getCurrentAmount())) {
                    needUpdate = true;
                }

                if (needUpdate) {
                    log.info("update campaign: {}", donationCampaign.getId());
                    donationCampaign.setActive(false);
                    donationCampaign.setUpdatedAt(new Date());
                    donationCampaign.setUpdatedBy("scheduler.system@alsvietnam.org");
                    donationCampaignRepository.save(donationCampaign);
                }
                index++;
            }
        }
        log.info("==========================================================================");
    }

    @Scheduled(cron = "0 0 0 1/1 * ?") // scheduler run every day (rate * second * minute): đơn vị là ms
    public void removeRequestVolunteerExpired() {
        log.info("*** Begin scheduler remove request member expired at {} ***",
                DateUtil.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
        ParameterSearchUser searchUser = new ParameterSearchUser();
        searchUser.setApproveStatus(false);
        searchUser.setCreatedTo(DateUtil.getDateBeforeNumberTimes(new Date(), 1, DateUtil.DAILY));
        List<User> users = userRepository.searchUsers(searchUser).getData();
        log.info("Expired request users: {}", users.size());
        searchUser.setDeleted(true);
        users.addAll(userRepository.searchUsers(searchUser).getData());
        log.info("Denied request users: {}", users.size());
        userRepository.deleteAll(users);
        log.info("==========================================================================");
    }
}
