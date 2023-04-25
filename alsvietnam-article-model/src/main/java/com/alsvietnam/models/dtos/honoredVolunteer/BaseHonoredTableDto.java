package com.alsvietnam.models.dtos.honoredVolunteer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:24 PM
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class BaseHonoredTableDto {

    @NotBlank(message = "title is required")
    private String title;

    @NotNull(message = "quarter is required")
    @Min(value = 1, message = "min quarter is 1")
    @Max(value = 4, message = "max quarter is 4")
    private Long quarter;

    @NotNull(message = "year is required")
    @Min(value = 2000, message = "min year is 2000")
    @Max(value = 9999, message = "max year is 9999")
    private Long year;

    private boolean active;

}
