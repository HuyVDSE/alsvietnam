package com.alsvietnam.repository;

import com.alsvietnam.entities.DonationCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 12:14 AM
 */

public interface DonationCampaignRepository extends JpaRepository<DonationCampaign, String>, DonationCampaignRepositoryCustom {
}
