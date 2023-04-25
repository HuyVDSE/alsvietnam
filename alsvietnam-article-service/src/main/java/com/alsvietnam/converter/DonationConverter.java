package com.alsvietnam.converter;

import com.alsvietnam.entities.Donation;
import com.alsvietnam.models.dtos.payment.DonationCustomDTO;
import com.alsvietnam.models.dtos.payment.DonationDto;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 8/23/2022
 * Time: 11:11 PM
 */

@Component
@ExtensionMethod(value = Extensions.class)
public class DonationConverter {

    public List<DonationDto> toDTO(List<Donation> donations) {
        return donations.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public DonationDto toDTO(Donation donation) {
        return DonationDto.builder()
                .id(donation.getId())
                .firstName(donation.getFirstName())
                .middleName(donation.getMiddleName())
                .lastName(donation.getLastName())
                .email(donation.getEmail())
                .phone(donation.getPhone())
                .paymentDate(donation.getPaymentDate() != null ? DateUtil.convertDateToString(donation.getPaymentDate(), DateUtil.TYPE_FORMAT_1) : null)
                .userId(donation.getUser() != null ? donation.getUser().getId() : null)
                .transactionId(donation.getTransactionId())
                .amount(donation.getAmount())
                .status(donation.getStatus())
                .createdAt(DateUtil.convertDateToString(donation.getCreatedAt(), DateUtil.TYPE_FORMAT_1))
                .generalFund(donation.getGeneralFund())
                .websiteFund(donation.getWebsiteFund())
                .documentFund(donation.getDocumentFund())
                .storyFund(donation.getStoryFund())
                .donationType(donation.getDonationType())
                .donationCampaignId(donation.getDonationCampaign() != null ? donation.getDonationCampaign().getId() : null)
                .paymentGateway(donation.getPaymentGateway())
                .hiddenInfo(donation.getHiddenInfo())
                .build();
    }

    public List<DonationCustomDTO> toDonationCustomDTO(List<Donation> donations) {
        if (donations.isNullOrEmpty()) return Collections.emptyList();
        return donations.stream().map(DonationCustomDTO::new).collect(Collectors.toList());
    }
}
