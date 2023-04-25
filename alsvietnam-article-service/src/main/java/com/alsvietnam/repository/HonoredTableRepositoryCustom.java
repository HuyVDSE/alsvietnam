package com.alsvietnam.repository;

import com.alsvietnam.entities.HonoredTable;
import com.alsvietnam.models.search.ParameterSearchHonoredTable;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 11/2/2022
 * Time: 9:39 PM
 */

public interface HonoredTableRepositoryCustom {

    ListWrapper<HonoredTable> searchHonoredTable(ParameterSearchHonoredTable searchParameter);
}
