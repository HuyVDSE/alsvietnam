package com.alsvietnam.models.dtos.campaign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 12:23 AM
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CreateCampaignDto {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "description is required")
    private String description;

    private String coverImage;

    private List<String> subImages;

    private String dateStart;

    private String dateEnd;

    private BigDecimal expectedAmount;

    private BigDecimal currentAmount;

    private boolean active = false;

}
