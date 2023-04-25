package com.alsvietnam.service.impl;

import com.alsvietnam.entities.Role;
import com.alsvietnam.entities.User;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.role.RoleDto;
import com.alsvietnam.models.search.ParameterSearchRole;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.RoleService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 8:20 PM
 */

@Service
@ExtensionMethod(Extensions.class)
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl extends BaseService implements RoleService {

    @Override
    public ListWrapper<Role> searchRole(ParameterSearchRole parameterSearch) {
        ListWrapper<Role> wrapper = roleRepository.searchRole(parameterSearch);

        return ListWrapper.<Role>builder()
                .total(wrapper.getTotal())
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .totalPage(wrapper.getTotalPage())
                .data(wrapper.getData())
                .build();
    }

    @Override
    public Role createRole(String name, String label) {
        log.info("Create new role: {}", name);
        Optional<Role> existedRole = roleRepository.findByName(name);
        if (existedRole.isPresent()) {
            throw new ServiceException("role " + name + " is already existed");
        }
        Role role = Role.builder()
                .name(name)
                .label(label)
                .createdAt(new Date())
                .createdBy(getCurrentUsername())
                .build();
        roleRepository.save(role);
        logDataService.create(role.getId(), EnumConst.LogTypeEnum.ROLE.name(), "Create new role: " + name);
        return role;
    }

    @Override
    public Role updateRole(RoleDto roleDto) {
        log.info("Update role: {}", roleDto.getName());
        Role role = roleRepository.findById(roleDto.getId())
                .orElseThrow(() -> new ServiceException("Role not found"));
        role.setName(roleDto.getName());
        role.setLabel(roleDto.getLabel());
        logDataService.create(role.getId(), EnumConst.LogTypeEnum.ROLE.name(), "Update role: " + role.getId());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Role role) {
        log.info("Delete role: {}", role.getName());
        List<User> usersByRole = userRepository.findByRole_Name(role.getName());
        if (!usersByRole.isEmpty()) {
            throw new ServiceException("Can't delete, role " + role.getName() + " is used by users");
        }
        logDataService.create(role.getId(), EnumConst.LogTypeEnum.ROLE.name(), "role_id " + role.getId() + " deleted");
        roleRepository.delete(role);
    }
}
