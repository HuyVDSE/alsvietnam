package com.alsvietnam.converter;

import com.alsvietnam.entities.Article;
import com.alsvietnam.entities.Task;
import com.alsvietnam.models.dtos.task.CreateTaskDto;
import com.alsvietnam.models.profiles.TaskProfile;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 9/16/2022
 * Time: 11:17 AM
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskConverter extends BaseConverter {

    private final TeamConverter teamConverter;
    private final UserConverter userConverter;
    private final ArticleConverter articleConverter;

    public Task fromCreateDTO(CreateTaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        if (Extensions.isBlankOrNull(taskDto.getStatus())) {
            task.setStatus(EnumConst.TaskStatusEnum.NEW.name());
        } else {
            task.setStatus(taskDto.getStatus());
        }
        task.setCreatedAt(new Date());
        task.setCreatedBy(userService.getUsernameLogin());
        return task;
    }

    public List<TaskProfile> toProfiles(List<Task> tasks, ParameterSearchTask parameterSearchTask) {
        return tasks.stream().map(task -> toProfile(task, parameterSearchTask)).collect(Collectors.toList());
    }

    public TaskProfile toProfile(Task task, ParameterSearchTask searchTask) {
        TaskProfile taskProfile = new TaskProfile();
        taskProfile.setId(task.getId());
        taskProfile.setName(task.getName());
        taskProfile.setDescription(task.getDescription());
        taskProfile.setStatus(task.getStatus());
        taskProfile.setStartDate(DateUtil.convertDateToString(task.getStartDate(), DateUtil.TYPE_FORMAT_1));
        taskProfile.setEndDate(DateUtil.convertDateToString(task.getEndDate(), DateUtil.TYPE_FORMAT_1));
        taskProfile.setActualStartDate(task.getActualStartDate() != null
                ? DateUtil.convertDateToString(task.getActualStartDate(), DateUtil.TYPE_FORMAT_1) : null);
        taskProfile.setActualEndDate(task.getActualEndDate() != null
                ? DateUtil.convertDateToString(task.getActualEndDate(), DateUtil.TYPE_FORMAT_1) : null);
        taskProfile.setTeam(teamConverter.toDTO(task.getTeam()));
        taskProfile.setManager(userConverter.toUserDTO(task.getManager(), false, false));
        if (task.getUsers() != null)
            taskProfile.setUsers(userConverter.toUserDTO(task.getUsers(), false, false));
        taskProfile.setNote(task.getNote());
        Article article = task.getArticle();
        if (article != null) {
            taskProfile.setArticleId(article.getId());
            if (searchTask.isBuildArticle()) {
                taskProfile.setArticleProfile(articleConverter.toProfile(article));
            }
        }
        return taskProfile;
    }

}
