package com.alsvietnam.models.dtos.honoredVolunteer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:26 PM
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CreateHonoredTableDto extends BaseHonoredTableDto {

    @NotNull(message = "honored_users is required")
    private List<HonoredUserDto> honoredUsers;

}
