package com.alsvietnam.service.impl;

import com.alsvietnam.entities.LogData;
import com.alsvietnam.service.LogDataService;
import com.alsvietnam.service.UserService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 11:22 PM
 */

@Service
@RequiredArgsConstructor
public class LogDataServiceImpl extends BaseService implements LogDataService {

    @Override
    public LogData create(String parentId, String type, String content) {
        String createdBy = getCurrentUsername();
        if (Extensions.isBlankOrNull(createdBy)) {
            createdBy = "system";
        }
        LogData logData = LogData.builder()
                .parentId(parentId)
                .type(type)
                .createdBy(createdBy)
                .createdAt(new Date())
                .content(content)
                .build();
        return logDataRepository.save(logData);
    }
}
