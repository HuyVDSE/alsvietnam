package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.user.*;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.search.ParameterSearchUser;
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
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/3/2022
 * Time: 11:32 PM
 */

@RestController
@RequestMapping(Constants.USER_SERVICE)
@ExtensionMethod(Extensions.class)
@Slf4j
@Tag(name = "User", description = "User API")
public class UserController extends BaseController {

    @Operation(summary = "Danh sách user", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
    })
    @GetMapping
    public ListWrapper<UserDto> getUsers(@RequestParam(value = "id", required = false) String id,
                                         @RequestParam(value = "ids", required = false) @Parameter(description = "List id: id1,id2,id3") String idArray,
                                         @RequestParam(value = "username", required = false) String username,
                                         @RequestParam(value = "email", required = false) String email,
                                         @RequestParam(value = "phone", required = false) String phone,
                                         @RequestParam(value = "team", required = false) String teamName,
                                         @RequestParam(value = "roles", required = false) @Parameter(description = "List role: id1,id2,id3") String roleArray,
                                         @RequestParam(value = "deleted", required = false) Boolean deleted,
                                         @RequestParam(value = "status", required = false) Boolean status,
                                         @RequestParam(value = "approve_status", required = false) Boolean approveStatus,
                                         @RequestParam(value = "build_role", required = false) Boolean isBuildRole,
                                         @RequestParam(value = "build_team", required = false) Boolean isBuildTeam,
                                         @RequestParam(value = "sort_by", required = false) String sortBy,
                                         @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                         @RequestParam(value = "current_page", required = false)
                                         @Min(value = 1, message = "current_page phải lớn hơn 0")
                                         @Parameter(description = "Default: 1") Integer currentPage,
                                         @RequestParam(value = "page_size", required = false)
                                         @Min(value = 1, message = "page_size phải lớn hơn 0")
                                         @Max(value = 50, message = "page_size phải bé hơn 50")
                                         @Parameter(description = "Default: 10") Integer pageSize) {
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchUser searchParam = new ParameterSearchUser();
        searchParam.setId(id);
        if (!idArray.isBlankOrNull()) {
            searchParam.setIds(Arrays.asList(idArray.split(",")));
        }
        searchParam.setUsername(username);
        searchParam.setEmail(email);
        searchParam.setPhone(phone);
        searchParam.setTeamName(teamName);
        if (!Extensions.isBlankOrNull(roleArray)) {
            List<String> roles = Arrays.asList(roleArray.split(","));
            searchParam.setRoles(roles);
        }
        searchParam.setDeleted(deleted);
        searchParam.setStatus(status);
        searchParam.setApproveStatus(approveStatus);
        searchParam.setBuildRole(isBuildRole != null && isBuildRole);
        searchParam.setBuildTeam(isBuildTeam != null && isBuildTeam);
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        searchParam.setSortField(sortBy);
        if (!Extensions.isBlankOrNull(sortOrder)) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }

        return userService.searchUsers(searchParam);
    }

    @Operation(summary = "Lấy user theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable("id") String userId) {
        ParameterSearchUser searchUser = new ParameterSearchUser();
        searchUser.setId(userId);
        searchUser.setStartIndex(0L);
        searchUser.setPageSize(1);
        ListWrapper<UserDto> wrapper = userService.searchUsers(searchUser);
        List<UserDto> data = wrapper.getData();
        if (data.isNullOrEmpty()) {
            throw new ServiceException("User not found");
        }
        return data.get(0);
    }

    @Operation(summary = "Tạo user", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto createUser(@ModelAttribute @Valid CreateUserDto user, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return userService.createUser(user);
    }

    @Operation(summary = "Đăng kí volunteer", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @PostMapping("/get-involve")
    public UserDto createGetInvolveUser(@RequestBody @Valid CreateGetInvolveUserDto userDto) {
        return userService.createGetInvolve(userDto);
    }

    @Operation(summary = "Cập nhật role cho user", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    @PostMapping("/role")
    public Object updateUserRole(@RequestBody @Valid UpdateUserRole model) {
        userService.updateUserRole(model.getUserId(), model.getRoleId());
        return ObjectResponseWrapper.builder().status(1)
                .message("Update user role success!").build();
    }

    @Operation(summary = "Cập nhật user", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto updateUser(@ModelAttribute @Valid UpdateUserDto userDto,
                              @RequestParam(value = "update_request", required = false) Boolean updateRequest,
                              BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return userService.updateUser(userDto, updateRequest);
    }

    @Operation(summary = "Cập nhật mật khẩu user", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @PutMapping("/password")
    public Object updatePassword(@RequestBody @Valid UpdateUserPassword model) {
        userService.updatePassword(model);
        return ObjectResponseWrapper.builder().status(1)
                .message("Update password success!").build();
    }

    @Operation(summary = "Xoá user", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @Secured(EnumConst.RoleEnum.ADMIN)
    @DeleteMapping("{id}")
    public Object deleteUser(@PathVariable String id,
                             @RequestParam(name = "update_request", required = false) Boolean updateRequest) {
        userService.deleteUser(id, updateRequest);
        return ObjectResponseWrapper.builder().status(1)
                .message("Delete user success!").build();
    }

    @Operation(summary = "Xác thực email", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @PostMapping("/{userId}/verify-account/{verifyId}")
    public Object updateVerifyEmailVolunteer(@PathVariable("userId") String userId, @PathVariable("verifyId") String verifyId) {
        userService.verifyAccount(userId, verifyId);
        return ObjectResponseWrapper.builder().status(1)
                .message("Verify user success!").build();
    }

    @Operation(summary = "Gửi mail xác thực", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @PostMapping("/{userId}/resend-email")
    public Object updateVerifyEmailVolunteer(@PathVariable("userId") String userId) {
        userService.resendMail(userId);
        return ObjectResponseWrapper.builder().status(1)
                .message("Send mail success!").build();
    }

    @Operation(summary = "Thống kê task của mỗi user", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
    })
    @GetMapping("/statistic-task")
    public List<UserDto> statisticTaskByUser(@RequestParam(value = "done_task_from", required = false)
                                             @Parameter(description = "pattern: yyyy-MM-dd") String doneTaskFrom,
                                             @RequestParam(value = "done_task_to", required = false)
                                             @Parameter(description = "pattern: yyyy-MM-dd") String doneTaskTo,
                                             @RequestParam(value = "task_status", required = false) String taskStatus,
                                             @RequestParam(value = "build_team", required = false) Boolean buildTeam,
                                             @RequestParam(value = "build_role", required = false) Boolean buildRole) {
        ParameterSearchTask parameterSearchTask = new ParameterSearchTask();
        parameterSearchTask.setStatus(taskStatus);
        parameterSearchTask.setDoneTaskFrom(doneTaskFrom, DateUtil.TYPE_FORMAT_2);
        parameterSearchTask.setDoneTaskTo(doneTaskTo, DateUtil.TYPE_FORMAT_2);
        boolean isBuildTeam = buildTeam != null && buildTeam;
        boolean isBuildRole = buildRole != null && buildRole;
        return userService.statisticTask(parameterSearchTask, isBuildTeam, isBuildRole);
    }
}
