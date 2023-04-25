package com.alsvietnam.models.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 9/20/2022
 * Time: 8:33 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateTaskDto extends BaseTaskDto {

    @NotBlank(message = "Id is required")
    private String id;

    private String articleId;

}
