package com.alsvietnam.models.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * Duc_Huy
 * Date: 8/23/2022
 * Time: 10:32 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DonationDto extends CreateDonationDto {

    private String id;

    private String status;

    private String paymentDate;

    private String createdAt;

}
