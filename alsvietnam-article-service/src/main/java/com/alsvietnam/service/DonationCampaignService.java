package com.alsvietnam.service;

import com.alsvietnam.models.dtos.campaign.CampaignDto;
import com.alsvietnam.models.dtos.campaign.CreateCampaignDto;
import com.alsvietnam.models.search.ParameterSearchCampaign;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 12:19 AM
 */

public interface DonationCampaignService {

    ListWrapper<CampaignDto> searchCampaign(ParameterSearchCampaign parameterSearchCampaign);

    CampaignDto createCampaign(CreateCampaignDto createCampaignDto);

    CampaignDto updateCampaign(CampaignDto updateCampaignDto);

    void disableCampaign(String campaignId);

    CampaignDto updateActiveStatus(String campaignId, boolean isActive);
}
