package com.alsvietnam.service.strategy;

import com.alsvietnam.repository.DonationRepository;
import com.alsvietnam.service.DonationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Duc_Huy
 * Date: 8/24/2022
 * Time: 11:25 AM
 */

public abstract class BaseStrategy {

    // variable
    protected ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // repository

    protected DonationRepository donationRepository;

    @Autowired
    public void setDonationRepository(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    // service

    protected DonationService donationService;

    @Autowired
    public void setDonationService(DonationService donationService) {
        this.donationService = donationService;
    }
}
