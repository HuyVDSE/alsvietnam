package com.alsvietnam.service;

import com.alsvietnam.models.dtos.honoredVolunteer.CreateHonoredTableDto;
import com.alsvietnam.models.dtos.honoredVolunteer.HonoredTableDto;
import com.alsvietnam.models.dtos.honoredVolunteer.UpdateHonoredTableDto;
import com.alsvietnam.models.search.ParameterSearchHonoredTable;
import com.alsvietnam.models.wrapper.ListWrapper;

import java.util.List;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:22 PM
 */

public interface HonoredTableService {

    List<HonoredTableDto> findAll();

    ListWrapper<HonoredTableDto> searchHonoredTable(ParameterSearchHonoredTable searchParameter);

    HonoredTableDto create(CreateHonoredTableDto honoredTableDto);

    HonoredTableDto update(String id, UpdateHonoredTableDto honoredTableDto);

    void delete(String honoredTableId);

    void publishHonor(String honoredTableId);
}
