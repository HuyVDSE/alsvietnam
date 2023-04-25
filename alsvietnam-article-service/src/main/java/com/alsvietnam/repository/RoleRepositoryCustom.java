package com.alsvietnam.repository;

import com.alsvietnam.entities.Role;
import com.alsvietnam.models.search.ParameterSearchRole;
import com.alsvietnam.models.wrapper.ListWrapper;

public interface RoleRepositoryCustom {

    ListWrapper<Role> searchRole(ParameterSearchRole parameterSearchRole);
}
