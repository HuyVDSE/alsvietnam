package com.alsvietnam.models.dtos.honoredVolunteer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:31 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class HonoredTableDto extends CreateHonoredTableDto {

    @NotBlank(message = "id is required")
    private String id;

    private boolean deleted;

    private Date createdAt;

    private String createdBy;

    private Date updatedAt;

    private String updatedBy;

}
