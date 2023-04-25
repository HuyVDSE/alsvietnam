package com.alsvietnam.models.dtos.task;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 12/4/2022
 * Time: 12:20 PM
 */

@Data
public class UpdateTaskStatusDto {

    @NotBlank(message = "status is required")
    private String status;

    private String note;

}
