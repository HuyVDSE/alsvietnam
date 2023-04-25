package com.alsvietnam.repository;

import com.alsvietnam.entities.TopOrganizationSupport;
import com.alsvietnam.models.search.ParameterSearchTopOrganizationSupport;
import com.alsvietnam.models.wrapper.ListWrapper;

public interface TopOrganizationSupportRepositoryCustom {
    ListWrapper<TopOrganizationSupport> searchTopOrganizationSupport(ParameterSearchTopOrganizationSupport parameterSearchTopOrganizationSupport);
}
