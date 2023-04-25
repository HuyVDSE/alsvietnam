package com.alsvietnam.converter;

import com.alsvietnam.entities.DonationCampaign;
import com.alsvietnam.models.dtos.campaign.CampaignDto;
import com.alsvietnam.models.dtos.campaign.CreateCampaignDto;
import com.alsvietnam.repository.DonationCampaignRepository;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.Extensions;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 9:38 PM
 */

@Component
@ExtensionMethod(Extensions.class)
@RequiredArgsConstructor
public class CampaignConverter extends BaseConverter {

    private final DonationCampaignRepository donationCampaignRepository;

    public DonationCampaign fromCreateDto(CreateCampaignDto campaignDto) {
        DonationCampaign donationCampaign = modelMapper.map(campaignDto, DonationCampaign.class);
        donationCampaign.setDeleted(false);
        donationCampaign.setCreatedAt(new Date());
        donationCampaign.setCreatedBy(userService.getUsernameLogin());
        return donationCampaign;
    }

    @SneakyThrows
    public CampaignDto toDto(DonationCampaign donationCampaign) {
        CampaignDto dto = modelMapper.map(donationCampaign, CampaignDto.class);
        if (donationCampaign.getDateStart() != null) {
            dto.setDateStart(DateUtil.convertDateToString(donationCampaign.getDateStart(), DateUtil.TYPE_FORMAT_1));
        }
        if (donationCampaign.getDateEnd() != null) {
            dto.setDateEnd(DateUtil.convertDateToString(donationCampaign.getDateEnd(), DateUtil.TYPE_FORMAT_1));
            Date currentEndDate = DateUtil.customToDate(new Date());
            if (donationCampaign.isActive() && donationCampaign.getDateEnd().before(currentEndDate)) {
                donationCampaign.setActive(false);
                donationCampaignRepository.save(donationCampaign);
            }
        }
        dto.setActive(donationCampaign.isActive());
        if (!Extensions.isBlankOrNull(donationCampaign.getSubImage())) {
            List<String> subImages = objectMapper.readValue(donationCampaign.getSubImage(), new TypeReference<>() {
            });
            dto.setSubImages(subImages);
        }
        return dto;
    }

    @SneakyThrows
    public List<CampaignDto> toDto(List<DonationCampaign> donationCampaigns) {
        return donationCampaigns.stream().map(this::toDto).collect(Collectors.toList());
    }

    @SneakyThrows
    public DonationCampaign fromUpdateDto(DonationCampaign donationCampaign, CampaignDto campaignDto) {
        modelMapper.map(campaignDto, donationCampaign);
        if (!campaignDto.getDateStart().isBlankOrNull()) {
            donationCampaign.setDateStart(DateUtil.formatDateString(campaignDto.getDateStart(), DateUtil.TYPE_FORMAT_1));
        }
        if (!campaignDto.getDateEnd().isBlankOrNull()) {
            donationCampaign.setDateEnd(DateUtil.formatDateString(campaignDto.getDateEnd(), DateUtil.TYPE_FORMAT_1));
        }
        if (!Extensions.isNullOrEmpty(campaignDto.getSubImages())) {
            donationCampaign.setSubImage(objectMapper.writeValueAsString(campaignDto.getSubImages()));
        } else {
            donationCampaign.setSubImage(null);
        }
        donationCampaign.setUpdatedAt(new Date());
        donationCampaign.setUpdatedBy(userService.getUsernameLogin());
        return donationCampaign;
    }
}
