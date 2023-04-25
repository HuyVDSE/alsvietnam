package com.alsvietnam.repository;

import com.alsvietnam.entities.Donation;
import com.alsvietnam.models.search.ParameterSearchDonation;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 8/23/2022
 * Time: 10:47 PM
 */

public interface DonationRepositoryCustom {

    ListWrapper<Donation> searchDonation(ParameterSearchDonation parameterSearchDonation);

}
