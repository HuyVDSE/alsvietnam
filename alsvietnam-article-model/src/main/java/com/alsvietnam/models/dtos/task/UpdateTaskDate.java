package com.alsvietnam.models.dtos.task;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 12/14/2022
 * Time: 9:41 PM
 */

@Data
public class UpdateTaskDate {

    @NotBlank(message = "endDate is required")
    private String endDate;
}
