package com.alsvietnam.models.dtos.campaign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 12:26 AM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDto extends CreateCampaignDto {

    private String id;

    private Long totalDonations;

}
