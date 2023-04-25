package com.alsvietnam.repository;

import com.alsvietnam.entities.Task;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 9/16/2022
 * Time: 12:19 PM
 */

public interface TaskRepositoryCustom {

    ListWrapper<Task> searchTask(ParameterSearchTask parameterSearchTask);
}
