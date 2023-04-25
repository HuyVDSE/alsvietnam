package com.alsvietnam.models.dtos.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 9/5/2022
 * Time: 11:37 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto extends CreateTeamDto {

    @NotBlank(message = "Id không được để trống")
    private String id;

}
