package com.alsvietnam.service.impl;

import com.alsvietnam.entities.HonoredTable;
import com.alsvietnam.entities.HonoredUser;
import com.alsvietnam.entities.User;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.honoredVolunteer.CreateHonoredTableDto;
import com.alsvietnam.models.dtos.honoredVolunteer.HonoredTableDto;
import com.alsvietnam.models.dtos.honoredVolunteer.HonoredUserDto;
import com.alsvietnam.models.dtos.honoredVolunteer.UpdateHonoredTableDto;
import com.alsvietnam.models.search.ParameterSearchHonoredTable;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.HonoredTableService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:22 PM
 */

@Service
@ExtensionMethod(Extensions.class)
@Slf4j
public class HonoredTableServiceImpl extends BaseService implements HonoredTableService {

    @Override
    public List<HonoredTableDto> findAll() {
        return honoredTableConverter.toDTO(honoredTableRepository.findAll(), new ParameterSearchHonoredTable());
    }

    @Override
    public ListWrapper<HonoredTableDto> searchHonoredTable(ParameterSearchHonoredTable searchParameter) {
        ListWrapper<HonoredTable> wrapper = honoredTableRepository.searchHonoredTable(searchParameter);
        List<HonoredTableDto> data = honoredTableConverter.toDTO(wrapper.getData(), searchParameter);
        return ListWrapper.<HonoredTableDto>builder()
                .data(data)
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .totalPage(wrapper.getTotalPage())
                .total(wrapper.getTotal())
                .build();
    }

    @Override
    @Transactional
    public HonoredTableDto create(CreateHonoredTableDto honoredTableDto) {
        log.info("Create Honored Table: {}", honoredTableDto.getTitle());

        boolean existHonoredTable = checkExistHonoredTable(honoredTableDto.getYear(), honoredTableDto.getQuarter());
        if (existHonoredTable) {
            throw new ServiceException("Already exist honored table in this time");
        }
        if (honoredTableDto.getHonoredUsers().isNullOrEmpty()) {
            throw new ServiceException("Honored user is required, please add more member");
        }

        HonoredTable honoredTable = honoredTableConverter.fromCreateDTO(honoredTableDto);
        setHonoredUser(honoredTable, honoredTableDto.getHonoredUsers());

        honoredTableRepository.save(honoredTable);
        logDataService.create(honoredTable.getId(), EnumConst.LogTypeEnum.HONORED_TABLE.name(), "create honored table " + honoredTable.getId());
        return honoredTableConverter.toDTO(honoredTable, new ParameterSearchHonoredTable());
    }

    @Override
    @Transactional
    public HonoredTableDto update(String id, UpdateHonoredTableDto honoredTableDto) {
        log.info("Update honored table {}", id);

        HonoredTable honoredTable = honoredTableRepository.findById(id)
                .orElseThrow(() -> new ServiceException("honored table not found"));

        boolean existHonoredTable = checkExistHonoredTable(honoredTableDto.getYear(), honoredTableDto.getQuarter());
        if (existHonoredTable) {
            throw new ServiceException("Already exist honored table in this time");
        }
        if (honoredTableDto.getHonoredUsers().isNullOrEmpty()) {
            throw new ServiceException("Honored user is required, please add more member");
        }

        honoredTableConverter.fromUpdateDTO(honoredTable, honoredTableDto);
        honoredUserRepository.deleteAllByHonoredTableId(id);
        setHonoredUser(honoredTable, honoredTableDto.getHonoredUsers());

        honoredTableRepository.save(honoredTable);
        logDataService.create(honoredTable.getId(), EnumConst.LogTypeEnum.HONORED_TABLE.name(), "update honored table " + honoredTable.getId());
        return honoredTableConverter.toDTO(honoredTable, new ParameterSearchHonoredTable());
    }

    @Override
    public void delete(String honoredTableId) {
        HonoredTable honoredTable = honoredTableRepository.findById(honoredTableId)
                .orElseThrow(() -> new ServiceException("honored table not found"));
        honoredTable.setDeleted(true);
        honoredTable.setUpdatedAt(new Date());
        honoredTable.setUpdatedBy(getCurrentUsername());
        honoredTableRepository.save(honoredTable);

        logDataService.create(honoredTableId, EnumConst.LogTypeEnum.HONORED_TABLE.name(), "delete honored table " + honoredTableId);
    }

    @Override
    public void publishHonor(String honoredTableId) {
        HonoredTable honoredTable = honoredTableRepository.findById(honoredTableId)
                .orElseThrow(() -> new ServiceException("Honored Table not found"));
        honoredTable.setActive(true);
        List<HonoredTable> honoredTablesNeedToDisable = honoredTableRepository.findByIdNotAndActive(honoredTableId, true);
        if (!honoredTablesNeedToDisable.isEmpty()) {
            for (HonoredTable honoredTable1 : honoredTablesNeedToDisable) {
                honoredTable1.setActive(false);
                honoredTableRepository.save(honoredTable1);
            }
        }
        honoredTableRepository.save(honoredTable);
    }

    // other ===========================================================================================================
    private boolean checkExistHonoredTable(Long year, Long quarter) {
        ParameterSearchHonoredTable searchParameter = new ParameterSearchHonoredTable();
        searchParameter.setQuarter(year);
        searchParameter.setYear(quarter);
        searchParameter.setStartIndex(0L);
        searchParameter.setPageSize(1);
        ListWrapper<HonoredTable> wrapper = honoredTableRepository.searchHonoredTable(searchParameter);
        return !wrapper.getData().isEmpty();
    }

    private void setHonoredUser(HonoredTable honoredTable, List<HonoredUserDto> honoredUsers) {
        List<String> userIds = honoredUsers.stream()
                .map(HonoredUserDto::getUserId).collect(Collectors.toList());
        Set<String> userIdSet = new HashSet<>();
        for (String userId : userIds) {
            if (userIdSet.contains(userId)) {
                throw new ServiceException("user " + userId + " already exist in list honored");
            }
            userIdSet.add(userId);
        }
        List<User> users = userRepository.findByIdIn(userIdSet);
        if (users.size() != userIdSet.size()) {
            Set<String> userQuerySet = users.stream().map(User::getId).collect(Collectors.toSet());
            for (String userId : userIdSet) {
                if (!userQuerySet.contains(userId)) {
                    throw new ServiceException("user " + userId + " not exist");
                }
            }
        }

        honoredTable.setHonoredUsers(new HashSet<>());
        for (HonoredUserDto honoredUserDto : honoredUsers) {
            HonoredUser honoredUser = honoredTableConverter.fromCreateDTO(honoredUserDto);
            honoredUser.setHonoredTable(honoredTable);
            for (User user : users) {
                if (user.getId().equals(honoredUserDto.getUserId())) {
                    honoredUser.setUser(user);
                    break;
                }
            }
            honoredTable.getHonoredUsers().add(honoredUser);
        }
    }
}
