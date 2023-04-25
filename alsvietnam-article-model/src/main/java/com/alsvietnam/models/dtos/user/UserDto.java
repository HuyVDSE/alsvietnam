package com.alsvietnam.models.dtos.user;

import com.alsvietnam.models.dtos.role.RoleDto;
import com.alsvietnam.models.dtos.team.TeamDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/6/2022
 * Time: 11:34 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserDto extends BaseUser {

    private String id;

    private String avatar;

    private String description;

    private RoleDto role;

    private List<TeamDto> teams;

    private String phone;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer numberTaskCompleted;

    public void addTaskCompleted(Integer number) {
        if (this.numberTaskCompleted == null) {
            this.numberTaskCompleted = 0;
        }
        this.numberTaskCompleted = numberTaskCompleted + number;
    }
}
