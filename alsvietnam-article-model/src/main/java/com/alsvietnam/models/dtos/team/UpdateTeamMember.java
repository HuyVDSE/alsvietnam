package com.alsvietnam.models.dtos.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Duc_Huy
 * Date: 9/8/2022
 * Time: 12:11 AM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeamMember {

    private String userId;

    @NotNull(message = "isAdd field is required")
    private Boolean isAdd;

}
