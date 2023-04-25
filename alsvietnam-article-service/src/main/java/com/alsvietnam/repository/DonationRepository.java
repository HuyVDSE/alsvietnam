package com.alsvietnam.repository;

import com.alsvietnam.entities.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Duc_Huy
 * Date: 8/23/2022
 * Time: 10:19 PM
 */

public interface DonationRepository extends JpaRepository<Donation, String>, DonationRepositoryCustom {

    List<Donation> findByStatusAndDonationCampaign_IdIn(String status, Collection<String> campaignIds);
}
