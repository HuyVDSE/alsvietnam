package com.alsvietnam.service.impl;

import com.alsvietnam.entities.TopOrganizationSupport;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.topOrganizationSupport.CreateTopOrganizationSupportDto;
import com.alsvietnam.models.dtos.topOrganizationSupport.UpdateTopOrganizationSupportDto;
import com.alsvietnam.models.search.ParameterSearchTopOrganizationSupport;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.TopOrganizationSupportService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@ExtensionMethod(value = Extensions.class)
@Slf4j
public class TopOrganizationSupportServiceImpl extends BaseService implements TopOrganizationSupportService {

    @Override
    public ListWrapper<TopOrganizationSupport> searchTopOrganizationSupport(ParameterSearchTopOrganizationSupport parameterSearchTopOrganizationSupport) {
        ListWrapper<TopOrganizationSupport> wrapper = topOrganizationSupportRepository.searchTopOrganizationSupport(parameterSearchTopOrganizationSupport);

        return ListWrapper.<TopOrganizationSupport>builder()
                .total(wrapper.getTotal())
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .totalPage(wrapper.getTotalPage())
                .data(wrapper.getData())
                .build();
    }

    @Override
    public TopOrganizationSupport createTopOrganizationSupport(CreateTopOrganizationSupportDto topOrganizationSupportDto) {
        log.info("Create top organization support");
        TopOrganizationSupport topOrganizationSupport = topOrganizationSupportConverter.fromCreateDto(topOrganizationSupportDto);
        if (!Extensions.isBlankOrNull(topOrganizationSupportDto.getImage())) {
            String imageName = topOrganizationSupportDto.getImage()
                    .substring(topOrganizationSupportDto.getImage().lastIndexOf("/") + 1);
            topOrganizationSupport.setImage(topOrganizationSupportDto.getImage());
            topOrganizationSupport.setImageName(imageName);
        }
        topOrganizationSupportRepository.save(topOrganizationSupport);
        logDataService.create(topOrganizationSupport.getId(), EnumConst.LogTypeEnum.TOP_ORGANIZATION_SUPPORT.name(),
                "Create top organization support " + topOrganizationSupport.getId() + " success");
        return topOrganizationSupport;
    }

    @Override
    public TopOrganizationSupport updateTopOrganizationSupport(UpdateTopOrganizationSupportDto updateTopOrganizationSupportDto) {
        log.info("Update top organization support: {}", updateTopOrganizationSupportDto.getId());
        TopOrganizationSupport topOrganizationSupport = topOrganizationSupportRepository.findById(updateTopOrganizationSupportDto.getId())
                .orElseThrow(() -> new ServiceException("Top Organization Support not found"));
        topOrganizationSupport = topOrganizationSupportConverter.fromUpdateDto(updateTopOrganizationSupportDto, topOrganizationSupport);
        if (!Extensions.isBlankOrNull(updateTopOrganizationSupportDto.getImage())) {
            fileStorageService.deleteFile(topOrganizationSupport.getImageName(), "general");
            String imageName = updateTopOrganizationSupportDto.getImage()
                    .substring(updateTopOrganizationSupportDto.getImage().lastIndexOf("/") + 1);
            topOrganizationSupport.setImage(updateTopOrganizationSupportDto.getImage());
            topOrganizationSupport.setImageName(imageName);
        }
        topOrganizationSupportRepository.save(topOrganizationSupport);
        logDataService.create(topOrganizationSupport.getId(), EnumConst.LogTypeEnum.TOP_ORGANIZATION_SUPPORT.name(),
                "Update top organization support " + topOrganizationSupport.getId() + " success");
        return topOrganizationSupport;
    }

    @Override
    public void disableTopOrganizationSupport(String topOrganizationSupportId) {
        log.info("Disable top organization support: {}", topOrganizationSupportId);
        TopOrganizationSupport topOrganizationSupport = topOrganizationSupportRepository.findById(topOrganizationSupportId)
                .orElseThrow(() -> new ServiceException("Top Organization Support not found"));
        topOrganizationSupport.setDeleted(true);
        topOrganizationSupport.setUpdatedAt(new Date());
        topOrganizationSupport.setCreatedBy(userService.getUsernameLogin());
        topOrganizationSupportRepository.save(topOrganizationSupport);
        logDataService.create(topOrganizationSupportId, EnumConst.LogTypeEnum.TOP_ORGANIZATION_SUPPORT.name(),
                "Disable donation campaign " + topOrganizationSupportId);
    }
}
