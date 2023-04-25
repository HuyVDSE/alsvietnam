package com.alsvietnam.models.dtos.task;

import com.alsvietnam.annotations.validator.ValueOfEnum;
import com.alsvietnam.utils.EnumConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 9/16/2022
 * Time: 9:40 AM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseTaskDto {

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    @ValueOfEnum(enumClass = EnumConst.TaskStatusEnum.class, message = "Task status is invalid")
    private String status;

    private String startDate;

    @NotBlank(message = "end date is required")
    private String endDate;

    private String note;

}
