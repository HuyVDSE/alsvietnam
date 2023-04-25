package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.Role;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.role.CreateRoleDto;
import com.alsvietnam.models.dtos.role.RoleDto;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.EnumConst;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 6:05 PM
 */

@RestController
@RequestMapping(Constants.ROLE_SERVICE)
@Tag(name = "Role", description = "Role API")
public class RoleController extends BaseController {

    @Operation(summary = "Xem danh sách Role", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Role.class))))
    })
    @GetMapping
    public List<Role> getRole(@RequestParam(value = "name", required = false) Optional<String> name,
                              @RequestParam(value = "names", required = false) Optional<List<String>> names) {
        if (name.isPresent()) {
            return roleRepository.findByNameContainsIgnoreCase(name.get());
        }
        if (names.isPresent()) {
            return roleRepository.findByNameIn(names.get());
        }
        return roleRepository.findAll();
    }

    @Operation(summary = "Lấy role theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Role.class)))
    })
    @GetMapping("{id}")
    public Role getRoleById(@PathVariable("id") String id) {
        return roleRepository.findById(id).orElseThrow(() -> new ServiceException("role not found"));
    }

    @Operation(summary = "Tạo Role", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Role.class)))
    })
    @Secured(EnumConst.RoleEnum.ADMIN)
    @PostMapping
    public Role createRole(@RequestBody CreateRoleDto roleDto) {
        return roleService.createRole(roleDto.getName(), roleDto.getLabel());
    }

    @Operation(summary = "Cập nhật Role", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Role.class)))
    })
    @Secured(EnumConst.RoleEnum.ADMIN)
    @PutMapping
    public Role updateRole(@RequestBody RoleDto roleDto) {

        return roleService.updateRole(roleDto);
    }

    @Operation(summary = "Xóa Role", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @Secured(EnumConst.RoleEnum.ADMIN)
    @DeleteMapping("{role_id}")
    public ObjectResponseWrapper deleteRole(@PathVariable(value = "role_id") String roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()) {
            throw new ServiceException("Role not found", HttpStatus.NOT_FOUND);
        }
        roleService.deleteRole(role.get());
        return ObjectResponseWrapper.builder().status(1)
                .data("role_id " + roleId + " deleted")
                .build();
    }

}
