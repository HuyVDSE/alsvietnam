package com.alsvietnam.repository;

import com.alsvietnam.entities.DonationCampaign;
import com.alsvietnam.models.search.ParameterSearchCampaign;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 12:15 AM
 */

public interface DonationCampaignRepositoryCustom {

    ListWrapper<DonationCampaign> searchCampaign(ParameterSearchCampaign parameterSearchCampaign);
}
