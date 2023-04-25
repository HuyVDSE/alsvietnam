package com.alsvietnam.service;

import com.alsvietnam.models.dtos.payment.CreateDonationDto;
import com.alsvietnam.models.dtos.payment.DonationCustomDTO;
import com.alsvietnam.models.dtos.payment.DonationDto;
import com.alsvietnam.models.profiles.DonationProfile;
import com.alsvietnam.models.search.ParameterSearchDonation;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 8/22/2022
 * Time: 12:09 AM
 */

public interface DonationService {

    ListWrapper<DonationDto> searchDonation(ParameterSearchDonation parameterSearchDonation);

    ListWrapper<DonationCustomDTO> searchDonationCustom(ParameterSearchDonation parameterSearchDonation);

    DonationProfile createDonation(CreateDonationDto donationDto);

    DonationDto updateDonation(DonationDto updateDonationDto);

    DonationDto changeStatusDonation(String id, String status);

}
