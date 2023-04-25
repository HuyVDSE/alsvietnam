package com.alsvietnam.service.impl;

import com.alsvietnam.entities.*;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.task.*;
import com.alsvietnam.models.profiles.TaskProfile;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.NotificationService;
import com.alsvietnam.service.TaskService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.service.strategy.mail.MailService;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 9/15/2022
 * Time: 11:20 PM
 */

@Service
@Transactional
@RequiredArgsConstructor
@ExtensionMethod(Extensions.class)
@Slf4j
public class TaskServiceImpl extends BaseService implements TaskService {

    private final NotificationService notificationService;

    private final MailService mailService;

    @Override
    public ListWrapper<TaskProfile> searchTask(ParameterSearchTask parameterSearchTask) {
        ListWrapper<Task> wrapper = taskRepository.searchTask(parameterSearchTask);
        List<TaskProfile> taskProfiles = taskConverter.toProfiles(wrapper.getData(), parameterSearchTask);

        return ListWrapper.<TaskProfile>builder()
                .data(taskProfiles)
                .total(wrapper.getTotal())
                .maxResult(wrapper.getMaxResult())
                .currentPage(wrapper.getCurrentPage())
                .totalPage(wrapper.getTotalPage())
                .build();
    }

    @Override
    public TaskProfile createTask(CreateTaskDto taskDto) {
        log.info("Create task with name: {}", taskDto.getName());
        TimeRange timeRange = validateTimeRange(taskDto.getStartDate(), taskDto.getEndDate());
        Team team = teamRepository.findById(taskDto.getTeamId())
                .orElseThrow(() -> new ServiceException("Team not found!"));
        User manager = userRepository.findById(taskDto.getManagerId())
                .orElseThrow(() -> new ServiceException("Manager not found!"));
        Role role = manager.getRole();
        if (!role.getName().equals(EnumConst.RoleEnum.ROLE_MANAGER.name())
                && !role.getName().equals(EnumConst.RoleEnum.ROLE_LEADER.name())) {
            throw new ServiceException(role.getLabel() + " don't have permission to manage task");
        }
        Task task = taskConverter.fromCreateDTO(taskDto);
        if (!taskDto.getArticleId().isBlankOrNull()) {
            Article article = articleRepository.findById(taskDto.getArticleId())
                    .orElseThrow(() -> new ServiceException("Article not found!"));
            task.setArticle(article);
        }
        List<User> users = null;
        if (!Extensions.isNullOrEmpty(taskDto.getUserIds())) {
            users = userRepository.findByIdIn(taskDto.getUserIds());
            Set<String> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
            taskDto.getUserIds().forEach(userId -> {
                if (!userIds.contains(userId)) {
                    throw new ServiceException("User " + userId + " not found!");
                }
            });
            users.forEach(user -> {
                if (!user.getTeams().contains(team)) {
                    throw new ServiceException("User " + user.getId() + " not in team " + team.getId());
                }
            });
            task.setUsers(new HashSet<>(users));
        }
        task.setStartDate(timeRange.getStartDate());
        task.setEndDate(timeRange.getEndDate());
        task.setTeam(team);
        task.setManager(manager);
        task.setNote(taskDto.getNote());
        taskRepository.save(task);

        if (users != null && !users.isEmpty()) {
            // send notification for member
            for (User user : users) {
                notificationService.createNotification(user.getId(), "New task assigned",
                        "You has been assigned to task: [" + task.getName() + "]");
                mailService.sendAssignTaskEmail(user.getEmail(), manager.getFullName(), user.getFullName(), task);
            }
        }
        ParameterSearchTask searchParam = new ParameterSearchTask();
        searchParam.setBuildArticle(false);
        return taskConverter.toProfile(task, searchParam);
    }

    @Override
    public TaskProfile updateTask(UpdateTaskDto taskDto) {
        log.info("Update task {}", taskDto.getId());
        Task task = taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> new ServiceException("Task Id " + taskDto.getId() + " not found"));
        Date startDateUpdate = validateStartDateUpdate(taskDto.getStartDate(), task.getStartDate());
        Date endDateUpdate = validateEndDateUpdate(taskDto.getEndDate(), task.getEndDate(), startDateUpdate);

        if (!taskDto.getArticleId().isBlankOrNull()
                && (task.getArticle() == null || !task.getArticle().getId().equals(taskDto.getArticleId()))) {
            if (!articleRepository.existsById(taskDto.getArticleId())) {
                throw new ServiceException("Article not found");
            }
            Article article = articleRepository.getById(taskDto.getArticleId()); // get article proxy
            task.setArticle(article);
        }

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStartDate(startDateUpdate);
        task.setEndDate(endDateUpdate);
        task.setUpdatedAt(new Date());
        task.setUpdatedBy(userService.getUsernameLogin());
        updateActualTimeOfTask(task, taskDto.getStatus());

        taskRepository.save(task);
        logDataService.create(task.getId(), EnumConst.LogTypeEnum.TASK.name(), "Update task " + task.getId());

        ParameterSearchTask searchParam = new ParameterSearchTask();
        searchParam.setBuildArticle(false);
        return taskConverter.toProfile(task, searchParam);
    }

    @Override
    public void deleteTask(String taskId) {
        log.info("Delete task {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ServiceException("Task not found"));
        logDataService.create(taskId, EnumConst.LogTypeEnum.TASK.name(), "Delete task " + taskId);
        taskRepository.delete(task);
    }

    @Override
    public void addMemberToTask(String taskId, String userId) {
        log.info("Add member {} to task {}", userId, taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ServiceException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found"));
        if (!user.getTeams().contains(task.getTeam())) {
            throw new ServiceException("User " + user.getUsername() + " not in team " + task.getTeam().getName());
        }

        // only accept 1 task doing by 1 person
        Role role = getCurrentUser().getRole();
        if (role.getName().equals(EnumConst.RoleEnum.ROLE_MANAGER.name())
                || role.getName().equals(EnumConst.RoleEnum.ROLE_LEADER.name())) {
            task.setUsers(Collections.singleton(user));
        } else {
            if (!task.getUsers().isNullOrEmpty()) {
                throw new ServiceException("Someone has already assigned this task");
            }
            task.addUser(user);
        }

        logDataService.create(taskId, EnumConst.LogTypeEnum.TASK.name(), "Member " + userId + " assigned to task " + taskId);

        // notification for user who added to this task
        notificationService.createNotification(user.getId(), "New task assigned",
                "You has been assigned to task: [" + task.getName() + "]");
        mailService.sendAssignTaskEmail(user.getEmail(), task.getManager().getFullName(), user.getFullName(), task);
    }

    @Override
    public void removeMemberFromTask(String taskId, String userId) {
        log.info("Remove member {} from task {}", userId, taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ServiceException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found"));
        if (task.getUsers() != null && !task.getUsers().contains(user)) {
            throw new ServiceException("Member not doing this task");
        }
        task.removeUser(user);
        taskRepository.save(task);
        logDataService.create(taskId, EnumConst.LogTypeEnum.TASK.name(), "Remove member " + user.getUsername() + " from task " + taskId);

        // notification for user who removed from this task
        notificationService.createNotification(user.getId(), "Task Unassigned",
                "You has been unassigned from task: [" + task.getName() + "]");
        mailService.sendUnAssignTaskEmail(user.getEmail(), task.getManager().getFullName(), user.getFullName(), task);
    }

    @Override
    public void updateTaskStatus(String taskId, UpdateTaskStatusDto model) {
        String status = model.getStatus();
        log.info("Update task {} status to {}", taskId, status);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ServiceException("Task not found"));
        try {
            EnumConst.TaskStatusEnum.valueOf(status);
        } catch (Exception e) {
            throw new ServiceException("Invalid task status");
        }
        updateActualTimeOfTask(task, status);
        task.setNote(model.getNote());
        taskRepository.save(task);
        logDataService.create(taskId, EnumConst.LogTypeEnum.TASK.name(), "Update task " + taskId + " status to " + status);

        // notification for manager when task is done
        if (status.equals(EnumConst.TaskStatusEnum.DONE.name())) {
            User manager = task.getManager();
            notificationService.createNotification(manager.getId(), "Task Finished",
                    "Task: [" + task.getName() + "] has finished");
            mailService.sendFinishTaskEmail(manager.getEmail(), manager.getFullName(), task);
        }
    }

    @Override
    public void updateTaskManager(String taskId, String managerId) {
        log.info("Update task {} manager to {}", taskId, managerId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ServiceException("Task not found"));
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ServiceException("User not found"));
        task.setManager(manager);
        taskRepository.save(task);
        logDataService.create(taskId, EnumConst.LogTypeEnum.TASK.name(), "Update task " + taskId + " manager to " + managerId);

        // notification for new manager
        mailService.sendAssignManageTask(manager.getEmail(), getCurrentUser().getFullName(), task);
    }

    @Override
    public void updateTaskDate(String taskId, UpdateTaskDate model) {
        log.info("Update date of task {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ServiceException("Task not found"));
        Date endDate = DateUtil.formatDateString(model.getEndDate(), DateUtil.TYPE_FORMAT_1);
        task.setEndDate(endDate);
        logDataService.create(taskId, EnumConst.LogTypeEnum.TASK.name(), "Update date of task " + taskId);
    }

    @Override
    public TaskStatisticDto statisticTask(ParameterSearchTask parameterSearchTask) {
        TaskStatisticDto dto = new TaskStatisticDto();
        List<String> taskStatuses = List.of(
                EnumConst.TaskStatusEnum.NEW.name(),
                EnumConst.TaskStatusEnum.DOING.name(),
                EnumConst.TaskStatusEnum.DONE.name()
        );
        parameterSearchTask.setStatuses(taskStatuses);
        parameterSearchTask.setBuildTeam(false);
        parameterSearchTask.setBuildManager(false);
        parameterSearchTask.setBuildUsersDoTask(false);
        List<Task> tasks = taskRepository.searchTask(parameterSearchTask).getData();
        if (tasks.isNullOrEmpty()) {
            return dto;
        }
        Date currentDate = DateUtil.customToDate(new Date());
        for (Task task : tasks) {
            switch (EnumConst.TaskStatusEnum.valueOf(task.getStatus())) {
                case NEW:
                    dto.addTaskNew(1);
                    break;
                case DOING:
                    dto.addTaskDoing(1);
                    break;
                case DONE:
                    dto.addTaskDone(1);
                    break;
            }

            if (task.getEndDate() != null && task.getEndDate().before(currentDate)
                    && !task.getStatus().equals(EnumConst.TaskStatusEnum.DONE.name())) {
                dto.addTaskDeadline(1);
            }
        }
        return dto;
    }

    // private function ================================================================================================
    private void updateActualTimeOfTask(Task task, String newStatus) {
        String oldStatus = task.getStatus();
        if (oldStatus.equals(newStatus)) {
            return;
        }

        switch (EnumConst.TaskStatusEnum.valueOf(newStatus)) {
            case DOING:
                if (task.getActualStartDate() == null) {
                    task.setActualStartDate(new Date());
                }
                break;
            case DONE:
                task.setActualEndDate(new Date());
                break;
        }
        task.setStatus(newStatus);
    }

    private Date validateStartDateUpdate(String startDateUpdate, Date startDateOld) {
        Date startDate = DateUtil.formatDateString(startDateUpdate, DateUtil.TYPE_FORMAT_1);
        startDate = DateUtil.customFromDate(startDate);
        if (!startDate.equals(startDateOld) && startDate.before(DateUtil.customFromDate(new Date()))) {
            throw new ServiceException("Start date must after or equal current date");
        }
        return startDate;
    }

    private Date validateEndDateUpdate(String endDateUpdate, Date endDateOld, Date startDate) {
        Date end = endDateOld;
        if (!Extensions.isBlankOrNull(endDateUpdate)) {
            end = DateUtil.formatDateString(endDateUpdate, DateUtil.TYPE_FORMAT_1);
            end = DateUtil.customToDate(end);
            if (endDateOld == null) {
                return end;
            }
            if (!end.equals(endDateOld) && startDate.after(end) || startDate.equals(end)) {
                throw new ServiceException("End date must after Begin date");
            }
        }
        return end;
    }
}
