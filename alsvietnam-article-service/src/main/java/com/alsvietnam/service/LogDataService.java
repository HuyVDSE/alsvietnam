package com.alsvietnam.service;

import com.alsvietnam.entities.LogData;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 11:22 PM
 */
public interface LogDataService {

    LogData create(String parentId, String type, String content);

}
