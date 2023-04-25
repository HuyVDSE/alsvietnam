package com.alsvietnam.models.dtos.honoredVolunteer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Duc_Huy
 * Date: 11/6/2022
 * Time: 10:29 PM
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UpdateHonoredTableDto extends CreateHonoredTableDto {

    private boolean deleted;
}
