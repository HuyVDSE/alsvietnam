package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.task.*;
import com.alsvietnam.models.profiles.TaskProfile;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/17/2022
 * Time: 11:22 PM
 */

@RestController
@RequestMapping(Constants.TASK_SERVICE)
@ExtensionMethod(Extensions.class)
@Tag(name = "Task", description = "Task API")
@Slf4j
public class TaskController extends BaseController {

    @SneakyThrows
    @Operation(summary = "Danh sách task", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskProfile.class))))
    })
    @GetMapping
    public ListWrapper<TaskProfile> getTasks(@RequestParam(value = "id", required = false) String id,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "team_id", required = false) String teamId,
                                             @RequestParam(value = "article_id", required = false) String articleId,
                                             @RequestParam(value = "manager_id", required = false) String managerId,
                                             @RequestParam(value = "user_id", required = false) String userId,
                                             @RequestParam(value = "from_start_date", required = false) String fromStartDate,
                                             @RequestParam(value = "to_start_date", required = false) String toStartDate,
                                             @RequestParam(value = "from_end_date", required = false) String fromEndDate,
                                             @RequestParam(value = "to_end_date", required = false) String toEndDate,
                                             @RequestParam(value = "build_article", required = false) Boolean buildArticle,
                                             @RequestParam(value = "sort_by", required = false) String sortBy,
                                             @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                             @RequestParam(value = "current_page", required = false)
                                             @Min(value = 1, message = "current_page phải lớn hơn 0")
                                             @Parameter(description = "Default: 1") Integer currentPage,
                                             @RequestParam(value = "page_size", required = false)
                                             @Min(value = 1, message = "page_size phải lớn hơn 0")
                                             @Parameter(description = "Default: 20") Integer pageSize) {
        log.info("Get tasks");
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchTask searchParam = new ParameterSearchTask();
        searchParam.setId(id);
        searchParam.setStatus(status);
        searchParam.setTeamId(teamId);
        searchParam.setArticleId(articleId);
        searchParam.setManagerId(managerId);
        searchParam.setUserId(userId);
        searchParam.setFromStartDate(fromStartDate);
        searchParam.setToStartDate(toStartDate);
        searchParam.setFromEndDate(fromEndDate);
        searchParam.setToEndDate(toEndDate);
        searchParam.setBuildArticle(buildArticle != null && buildArticle);
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        searchParam.setSortField(sortBy);
        if (!sortOrder.isBlankOrNull()) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }

        return taskService.searchTask(searchParam);
    }

    @Operation(summary = "Lấy task theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TaskProfile.class)))
    })
    @GetMapping("{id}")
    public TaskProfile getTaskById(@PathVariable("id") String id,
                                   @RequestParam(value = "build_article", required = false) Boolean buildArticle) {
        log.info("Get task by id");
        ParameterSearchTask searchParam = new ParameterSearchTask();
        searchParam.setId(id);
        searchParam.setBuildArticle(buildArticle != null && buildArticle);
        searchParam.setStartIndex(0L);
        searchParam.setPageSize(1);
        ListWrapper<TaskProfile> wrapper = taskService.searchTask(searchParam);
        List<TaskProfile> data = wrapper.getData();
        if (Extensions.isNullOrEmpty(data)) {
            throw new ServiceException("task not found");
        }
        return data.get(0);
    }

    @Operation(summary = "Tạo task", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TaskProfile.class)))
    })
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.LEADER
    })
    @PostMapping
    public TaskProfile createTask(@RequestBody @Valid CreateTaskDto model) {
        return taskService.createTask(model);
    }

    @Operation(summary = "Xoá task", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @DeleteMapping("{id}")
    public Object deleteTask(@PathVariable("id") String taskId) {
        taskService.deleteTask(taskId);
        return ObjectResponseWrapper.builder().status(1)
                .data("taskId " + taskId + " deleted").build();
    }

    @Operation(summary = "Cập nhật task", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TaskProfile.class)))
    })
    @PutMapping
    public TaskProfile updateTask(@RequestBody @Valid UpdateTaskDto model) {
        return taskService.updateTask(model);
    }

    @Operation(summary = "Cập nhật trạng thái task", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @PutMapping("{id}/status")
    public Object updateTaskStatus(@PathVariable("id") String taskId,
                                   @RequestBody @Valid UpdateTaskStatusDto model) {
        taskService.updateTaskStatus(taskId, model);
        return ObjectResponseWrapper.builder().status(1)
                .data("taskId " + taskId + " updated status to " + model.getStatus()).build();
    }

    @Operation(summary = "Thêm member vào task", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @PostMapping("{id}/members/{member_id}")
    public Object addMemberToTask(@PathVariable("id") String taskId,
                                  @PathVariable("member_id") String memberId) {
        taskService.addMemberToTask(taskId, memberId);
        return ObjectResponseWrapper.builder().status(1)
                .data("Assign to task successfully").build();
    }

    @Operation(summary = "Xoá member khỏi task", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.LEADER
    })
    @DeleteMapping("{id}/members/{member_id}")
    public Object removeMemberFromTask(@PathVariable("id") String taskId,
                                       @PathVariable("member_id") String memberId) {
        taskService.removeMemberFromTask(taskId, memberId);
        return ObjectResponseWrapper.builder().status(1)
                .data("member removed from task successfully").build();
    }

    @Operation(summary = "Cập nhật người quản lý task", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @PutMapping("{id}/managers/{manager_id}")
    public Object updateTaskManager(@PathVariable("id") String taskId,
                                    @PathVariable("manager_id") String managerId) {
        taskService.updateTaskManager(taskId, managerId);
        return ObjectResponseWrapper.builder().status(1)
                .data("update manager " + managerId + " for task " + taskId + " success").build();
    }

    @Operation(summary = "Thống kê trạng thái task", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TaskStatisticDto.class)))
    })
    @GetMapping("/statistic")
    public TaskStatisticDto statisticTask(@RequestParam(value = "team_id", required = false) String teamId,
                                          @RequestParam(value = "user_id", required = false) String userId,
                                          @RequestParam(value = "from_start_date", required = false) String fromStartDate,
                                          @RequestParam(value = "to_start_date", required = false) String toStartDate,
                                          @RequestParam(value = "from_end_date", required = false) String fromEndDate,
                                          @RequestParam(value = "to_end_date", required = false) String toEndDate,
                                          @RequestParam(value = "created_from", required = false) String createdFrom,
                                          @RequestParam(value = "created_to", required = false) String createdTo) {
        ParameterSearchTask searchParam = new ParameterSearchTask();
        searchParam.setTeamId(teamId);
        searchParam.setUserId(userId);
        searchParam.setFromStartDate(fromStartDate);
        searchParam.setToStartDate(toStartDate);
        searchParam.setFromEndDate(fromEndDate);
        searchParam.setToEndDate(toEndDate);
        searchParam.setCreatedFrom(createdFrom, DateUtil.TYPE_FORMAT_2);
        searchParam.setCreatedTo(createdTo, DateUtil.TYPE_FORMAT_2);
        return taskService.statisticTask(searchParam);
    }

    @PutMapping("{id}/end-date")
    public Object updateEndDateOfTask(@PathVariable("id") String id,
                                      @RequestBody @Valid UpdateTaskDate model) {
        taskService.updateTaskDate(id, model);
        return ObjectResponseWrapper.builder().status(1)
                .data("Update date of task success").build();
    }
}
