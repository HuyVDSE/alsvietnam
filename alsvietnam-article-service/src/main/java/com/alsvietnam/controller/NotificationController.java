package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.Notification;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.notification.NotificationDto;
import com.alsvietnam.models.search.ParameterSearchNotification;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.Extensions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(Constants.NOTIFICATION_SERVICE)
@ExtensionMethod(Extensions.class)
@Slf4j
@Tag(name = "Notification", description = "Notification API")
public class NotificationController extends BaseController {
    @GetMapping
    @Operation(summary = "Danh sách notification", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Notification.class))))
    })
    public ListWrapper<NotificationDto> getNotification(@RequestParam(value = "id", required = false) String id,
                                                        @RequestParam(value = "title", required = false) String title,
                                                        @RequestParam(value = "content", required = false) String content,
                                                        @RequestParam(value = "status", required = false) @Parameter(description = "Allowed values: true | false.") Boolean status,
                                                        @RequestParam(value = "createFrom", required = false) Long createFrom,
                                                        @RequestParam(value = "createTo", required = false) Long createTo,
                                                        @RequestParam(value = "userId", required = false) String userId,
                                                        @RequestParam(value = "sort_by", required = false) String sortBy,
                                                        @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                                        @RequestParam(value = "current_page", required = false)
                                                        @Min(value = 1, message = "current_page phải lớn hơn 0")
                                                        @Parameter(description = "Default: 1") Integer currentPage,
                                                        @RequestParam(value = "page_size", required = false)
                                                        @Min(value = 1, message = "page_size phải lớn hơn 0")
                                                        @Max(value = 50, message = "page_size phải bé hơn 50")
                                                        @Parameter(description = "Default: 10") Integer pageSize) {
        log.info("get top organization support");
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchNotification searchParam = new ParameterSearchNotification();
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        if (!Extensions.isBlankOrNull(sortOrder)) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }
        searchParam.setSortField(sortBy);
        searchParam.setId(id);
        searchParam.setStatus(status);
        searchParam.setTitle(title);
        searchParam.setContent(content);
        searchParam.setUserId(userId);
        if (createFrom != null) {
            searchParam.setCreateFrom(new Date(createFrom));
        }
        if (createTo != null) {
            searchParam.setCreateTo(new Date(createTo));
        }

        return notificationService.searchNotification(searchParam);
    }

    @Operation(summary = "Lấy notification theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Notification.class)))
    })
    @GetMapping("{id}")
    public NotificationDto getNotifiactionById(@PathVariable("id") String id) {
        ParameterSearchNotification searchParam = new ParameterSearchNotification();
        searchParam.setId(id);
        searchParam.setStartIndex(0L);
        searchParam.setPageSize(1);
        ListWrapper<NotificationDto> wrapper = notificationService.searchNotification(searchParam);
        List<NotificationDto> results = wrapper.getData();
        if (Extensions.isNullOrEmpty(results)) {
            throw new ServiceException("Top organization support not found");
        }
        return results.get(0);
    }

    @Operation(summary = "Update status notification theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @PutMapping("{id}")
    public ObjectResponseWrapper updateNotification(@PathVariable("id") String id) {
        notificationService.updateStatusNotification(id);
        return ObjectResponseWrapper.builder().status(1).
                message("Update status notification success").build();
    }
}
