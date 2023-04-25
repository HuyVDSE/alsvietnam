package com.alsvietnam.converter;

import com.alsvietnam.entities.TopOrganizationSupport;
import com.alsvietnam.models.dtos.topOrganizationSupport.CreateTopOrganizationSupportDto;
import com.alsvietnam.models.dtos.topOrganizationSupport.UpdateTopOrganizationSupportDto;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ExtensionMethod(value = Extensions.class)
public class TopOrganizationSupportConverter extends BaseConverter{

    public TopOrganizationSupport fromCreateDto(CreateTopOrganizationSupportDto topOrganizationSupportDto) {
        TopOrganizationSupport topOrganizationSupport = modelMapper.map(topOrganizationSupportDto, TopOrganizationSupport.class);
        topOrganizationSupport.setDeleted(false);
        topOrganizationSupport.setCreatedAt(new Date());
        topOrganizationSupport.setCreatedBy(userService.getUsernameLogin());
        return topOrganizationSupport;
    }

    public TopOrganizationSupport fromUpdateDto(UpdateTopOrganizationSupportDto topOrganizationSupportDto
            , TopOrganizationSupport topOrganizationSupport) {
        modelMapper.map(topOrganizationSupportDto, topOrganizationSupport);
        topOrganizationSupport.setUpdatedAt(new Date());
        topOrganizationSupport.setUpdatedBy(userService.getUsernameLogin());
        return topOrganizationSupport;
    }
}
