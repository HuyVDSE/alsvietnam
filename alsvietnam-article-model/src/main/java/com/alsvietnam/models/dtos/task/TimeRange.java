package com.alsvietnam.models.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Duc_Huy
 * Date: 9/20/2022
 * Time: 8:59 PM
 */

@Data
@AllArgsConstructor
public class TimeRange {

    private Date startDate;

    private Date endDate;

}
