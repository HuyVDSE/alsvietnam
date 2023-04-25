package com.alsvietnam.models.dtos.payment;

import com.alsvietnam.annotations.validator.ValueOfEnum;
import com.alsvietnam.utils.EnumConst;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Duc_Huy
 * Date: 8/23/2022
 * Time: 10:23 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateDonationDto {

    @NotBlank(message = "firstName is required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    private String email;

    private String phone;

    private BigDecimal amount;

    @NotBlank(message = "paymentGateway is required")
    @ValueOfEnum(enumClass = EnumConst.PaymentGatewayEnum.class, message = "paymentGateway allow values: MOMO | VNPAY")
    private String paymentGateway;

    private String paymentAccount;

    private String transactionId;

    @NotNull(message = "donationType is required")
    @ValueOfEnum(enumClass = EnumConst.DonationTypeEnum.class, message = "donationType allow values: GENERAL | CAMPAIGN")
    private String donationType;

    private String userId;

    private String donationCampaignId;

    @NotNull(message = "generalFund is required")
    private BigDecimal generalFund;

    @NotNull(message = "websiteFund is required")
    private BigDecimal websiteFund;

    @NotNull(message = "documentFund is required")
    private BigDecimal documentFund;

    @NotNull(message = "storyFund is required")
    private BigDecimal storyFund;

    private Boolean hiddenInfo = false;

}
