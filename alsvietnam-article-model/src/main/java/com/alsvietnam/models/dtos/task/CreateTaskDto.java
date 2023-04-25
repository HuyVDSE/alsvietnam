package com.alsvietnam.models.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/16/2022
 * Time: 9:56 AM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateTaskDto extends BaseTaskDto {

    @NotBlank(message = "teamId is required")
    private String teamId;

    private String articleId;

    @NotBlank(message = "managerId is required")
    private String managerId;

    private List<String> userIds;

}
