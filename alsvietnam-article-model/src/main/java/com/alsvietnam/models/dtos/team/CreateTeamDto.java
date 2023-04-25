package com.alsvietnam.models.dtos.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/5/2022
 * Time: 11:37 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamDto {

    @NotBlank(message = "Tên team không được để trống")
    private String name;

    private String leaderId;

}
