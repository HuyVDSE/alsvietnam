package com.alsvietnam.service;

import com.alsvietnam.entities.TopOrganizationSupport;
import com.alsvietnam.models.dtos.topOrganizationSupport.CreateTopOrganizationSupportDto;
import com.alsvietnam.models.dtos.topOrganizationSupport.UpdateTopOrganizationSupportDto;
import com.alsvietnam.models.search.ParameterSearchTopOrganizationSupport;
import com.alsvietnam.models.wrapper.ListWrapper;

public interface TopOrganizationSupportService {

    ListWrapper<TopOrganizationSupport> searchTopOrganizationSupport(ParameterSearchTopOrganizationSupport parameterSearchTopOrganizationSupport);

    TopOrganizationSupport createTopOrganizationSupport(CreateTopOrganizationSupportDto topOrganizationSupportDto);

    TopOrganizationSupport updateTopOrganizationSupport(UpdateTopOrganizationSupportDto topOrganizationSupportDto);

    void disableTopOrganizationSupport(String topOrganizationSupportId);
}
