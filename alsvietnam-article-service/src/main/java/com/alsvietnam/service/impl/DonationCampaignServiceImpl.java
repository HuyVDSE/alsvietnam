package com.alsvietnam.service.impl;

import com.alsvietnam.entities.Donation;
import com.alsvietnam.entities.DonationCampaign;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.campaign.CampaignDto;
import com.alsvietnam.models.dtos.campaign.CreateCampaignDto;
import com.alsvietnam.models.dtos.task.TimeRange;
import com.alsvietnam.models.search.ParameterSearchCampaign;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.DonationCampaignService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 12:19 AM
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ExtensionMethod(Extensions.class)
public class DonationCampaignServiceImpl extends BaseService implements DonationCampaignService {

    private final ObjectMapper objectMapper;

    @Override
    public ListWrapper<CampaignDto> searchCampaign(ParameterSearchCampaign parameterSearchCampaign) {
        ListWrapper<DonationCampaign> wrapper = donationCampaignRepository.searchCampaign(parameterSearchCampaign);

        List<CampaignDto> campaignDTOs = campaignConverter.toDto(wrapper.getData());

        // build total donations of each campaign
        List<String> campaignIds = campaignDTOs.stream().map(CampaignDto::getId).collect(Collectors.toList());
        List<Donation> donations = donationRepository.findByStatusAndDonationCampaign_IdIn(EnumConst.PaymentStatusEnum.SUCCESS.toString(), campaignIds);
        if (!donations.isNullOrEmpty()) {
            Map<String, Long> donationCountMap = donations.stream()
                    .collect(Collectors.groupingBy(donation -> donation.getDonationCampaign().getId(), Collectors.counting()));
            for (CampaignDto campaignDTO : campaignDTOs) {
                Long totalDonations = donationCountMap.get(campaignDTO.getId());
                campaignDTO.setTotalDonations(Objects.requireNonNullElse(totalDonations, 0L));
            }
        }

        return ListWrapper.<CampaignDto>builder()
                .total(wrapper.getTotal())
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .totalPage(wrapper.getTotalPage())
                .data(campaignDTOs)
                .build();
    }

    @SneakyThrows
    @Override
    public CampaignDto createCampaign(CreateCampaignDto createCampaignDto) {
        log.info("Create Campaign");
        DonationCampaign donationCampaign = campaignConverter.fromCreateDto(createCampaignDto);
        TimeRange timeRange = validateTimeRange(createCampaignDto.getDateStart(), createCampaignDto.getDateEnd());
        donationCampaign.setDateStart(DateUtil.customFromDate(timeRange.getStartDate()));
        if (donationCampaign.getDateStart().equals(DateUtil.customFromDate(new Date()))) {
            donationCampaign.setActive(true);
        }
        donationCampaign.setDateEnd(DateUtil.customToDate(timeRange.getEndDate()));
        if (!Extensions.isNullOrEmpty(createCampaignDto.getSubImages())) {
            String subImage = objectMapper.writeValueAsString(createCampaignDto.getSubImages());
            donationCampaign.setSubImage(subImage);
        }

        donationCampaignRepository.save(donationCampaign);
        logDataService.create(donationCampaign.getId(), EnumConst.LogTypeEnum.DONATION_CAMPAIGN.name(),
                "Create donation campaign " + donationCampaign.getId() + " success");
        return campaignConverter.toDto(donationCampaign);
    }

    @SneakyThrows
    @Override
    public CampaignDto updateCampaign(CampaignDto updateCampaignDto) {
        log.info("Update campaign: {}", updateCampaignDto.getId());
        DonationCampaign donationCampaign = donationCampaignRepository.findById(updateCampaignDto.getId())
                .orElseThrow(() -> new ServiceException("Donation campaign not found"));
        donationCampaign = campaignConverter.fromUpdateDto(donationCampaign, updateCampaignDto);
        donationCampaignRepository.save(donationCampaign);
        logDataService.create(donationCampaign.getId(), EnumConst.LogTypeEnum.DONATION_CAMPAIGN.name(),
                "Update donation campaign " + donationCampaign.getId() + " success");
        return campaignConverter.toDto(donationCampaign);
    }

    @SneakyThrows
    @Override
    public void disableCampaign(String campaignId) {
        log.info("Disable campaign: {}", campaignId);
        DonationCampaign donationCampaign = donationCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new ServiceException("Donation campaign not found"));
        donationCampaign.setDeleted(true);
        donationCampaign.setUpdatedAt(new Date());
        donationCampaign.setUpdatedBy(getCurrentUsername());
        logDataService.create(donationCampaign.getId(), EnumConst.LogTypeEnum.DONATION_CAMPAIGN.name(),
                "Disable donation campaign " + donationCampaign.getId());
        campaignConverter.toDto(donationCampaign);
    }

    @Override
    public CampaignDto updateActiveStatus(String campaignId, boolean isActive) {
        log.info("Update active status of campaign: {}", campaignId);
        DonationCampaign campaign = donationCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new ServiceException("Campaign not found"));
        campaign.setActive(isActive);
        campaign.setUpdatedAt(new Date());
        campaign.setUpdatedBy(getCurrentUsername());
        logDataService.create(campaignId, EnumConst.LogTypeEnum.DONATION_CAMPAIGN.name(),
                "Disable donation campaign " + campaignId);
        return campaignConverter.toDto(campaign);
    }

}
