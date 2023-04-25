package com.alsvietnam.service;

import com.alsvietnam.entities.Role;
import com.alsvietnam.models.dtos.role.RoleDto;
import com.alsvietnam.models.search.ParameterSearchRole;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 8:21 PM
 */
public interface RoleService {

    ListWrapper<Role> searchRole(ParameterSearchRole parameterSearchRole);

    Role createRole(String name, String label);

    Role updateRole(RoleDto roleDto);

    void deleteRole(Role role);

}
