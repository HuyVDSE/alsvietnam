package com.alsvietnam.models.profiles;

import com.alsvietnam.models.dtos.payment.DonationDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Duc_Huy
 * Date: 8/23/2022
 * Time: 10:35 PM
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationProfile {

    private DonationDto donation;

    @JsonProperty("payment_link")
    private String paymentLink;

}
