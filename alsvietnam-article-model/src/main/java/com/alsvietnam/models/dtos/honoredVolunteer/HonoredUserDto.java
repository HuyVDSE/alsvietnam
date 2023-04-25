package com.alsvietnam.models.dtos.honoredVolunteer;

import com.alsvietnam.models.dtos.user.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:27 PM
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HonoredUserDto {

    private String userId;

    private String role;

    private String medal;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private UserDto userDto;

}
