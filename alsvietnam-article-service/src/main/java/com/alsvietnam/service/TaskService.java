package com.alsvietnam.service;

import com.alsvietnam.models.dtos.task.*;
import com.alsvietnam.models.profiles.TaskProfile;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 9/15/2022
 * Time: 11:19 PM
 */
public interface TaskService {

    ListWrapper<TaskProfile> searchTask(ParameterSearchTask parameterSearchTask);

    TaskProfile createTask(CreateTaskDto taskDto);

    TaskProfile updateTask(UpdateTaskDto taskDto);

    void deleteTask(String taskId);

    void addMemberToTask(String taskId, String userId);

    void removeMemberFromTask(String taskId, String userId);

    void updateTaskStatus(String taskId, UpdateTaskStatusDto model);

    void updateTaskManager(String taskId, String managerId);

    void updateTaskDate(String taskId, UpdateTaskDate model);

    TaskStatisticDto statisticTask(ParameterSearchTask parameterSearchTask);
}
