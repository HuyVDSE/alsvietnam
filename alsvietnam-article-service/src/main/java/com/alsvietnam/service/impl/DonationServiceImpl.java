package com.alsvietnam.service.impl;

import com.alsvietnam.entities.*;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.payment.CreateDonationDto;
import com.alsvietnam.models.dtos.payment.DonationCustomDTO;
import com.alsvietnam.models.dtos.payment.DonationDto;
import com.alsvietnam.models.profiles.DonationProfile;
import com.alsvietnam.models.search.ParameterSearchDonation;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.DonationService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.service.strategy.payment.PaymentFactory;
import com.alsvietnam.service.strategy.payment.PaymentStrategy;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Duc_Huy
 * Date: 8/22/2022
 * Time: 12:10 AM
 */

@Service
@Slf4j
@ExtensionMethod(Extensions.class)
public class DonationServiceImpl extends BaseService implements DonationService {

    private PaymentFactory paymentFactory;

    @Autowired
    public void setPaymentFactory(PaymentFactory paymentFactory) {
        this.paymentFactory = paymentFactory;
    }

    @Override
    public ListWrapper<DonationDto> searchDonation(ParameterSearchDonation parameterSearchDonation) {
        log.info("GET donations");
        ListWrapper<Donation> wrapper = donationRepository.searchDonation(parameterSearchDonation);
        List<DonationDto> donationDTOs = donationConverter.toDTO(wrapper.getData());
        return ListWrapper.<DonationDto>builder()
                .total(wrapper.getTotal())
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .totalPage(wrapper.getTotalPage())
                .data(donationDTOs)
                .build();
    }

    @Override
    public ListWrapper<DonationCustomDTO> searchDonationCustom(ParameterSearchDonation parameterSearchDonation) {
        log.info("GET donations custom");
        ListWrapper<Donation> wrapper = donationRepository.searchDonation(parameterSearchDonation);
        List<DonationCustomDTO> donationCustomDTOs = new ArrayList<>();
        if (parameterSearchDonation.getBuildForDashboard() != null && parameterSearchDonation.getBuildForDashboard()
                && parameterSearchDonation.getDonationType() != null
                && parameterSearchDonation.getDonationType().equals(EnumConst.DonationTypeEnum.GENERAL.toString())) {
            BigDecimal totalGeneral = BigDecimal.ZERO;
            BigDecimal totalWebsite = BigDecimal.ZERO;
            BigDecimal totalDocument = BigDecimal.ZERO;
            BigDecimal totalStory = BigDecimal.ZERO;
            for (Donation donation : wrapper.getData()) {
                totalGeneral = totalGeneral.add(donation.getGeneralFund());
                totalWebsite = totalWebsite.add(donation.getWebsiteFund());
                totalDocument = totalDocument.add(donation.getDocumentFund());
                totalStory = totalStory.add(donation.getStoryFund());
            }
            donationCustomDTOs.add(new DonationCustomDTO(totalGeneral, totalWebsite, totalDocument, totalStory));
            return ListWrapper.<DonationCustomDTO>builder()
                    .total(1)
                    .currentPage(1)
                    .maxResult(1)
                    .totalPage(1)
                    .data(donationCustomDTOs)
                    .build();
        }

        donationCustomDTOs = donationConverter.toDonationCustomDTO(wrapper.getData());
        return ListWrapper.<DonationCustomDTO>builder()
                .total(wrapper.getTotal())
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .totalPage(wrapper.getTotalPage())
                .data(donationCustomDTOs)
                .build();
    }

    @Override
    @Transactional
    public DonationProfile createDonation(CreateDonationDto dto) {
        log.info("Create Donation");
        Donation donation = Donation.builder()
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .amount(dto.getAmount())
                .paymentGateway(dto.getPaymentGateway())
                .generalFund(dto.getGeneralFund())
                .websiteFund(dto.getWebsiteFund())
                .documentFund(dto.getDocumentFund())
                .storyFund(dto.getStoryFund())
                .status(EnumConst.PaymentStatusEnum.PENDING.name())
                .hiddenInfo(dto.getHiddenInfo())
                .createdBy(userService.getUsernameLogin())
                .createdAt(new Date())
                .build();

        // donation type
        if (dto.getDonationType().equals(EnumConst.DonationTypeEnum.GENERAL.name())) {
            BigDecimal totalFundAmount = dto.getGeneralFund().add(dto.getWebsiteFund()).add(dto.getDocumentFund()).add(dto.getStoryFund());
            if (!totalFundAmount.equals(dto.getAmount())) {
                throw new ServiceException("Total fund amount is not equal to donation amount");
            }
            donation.setDonationType(EnumConst.DonationTypeEnum.GENERAL.name());
        } else if (dto.getDonationType().equals(EnumConst.DonationTypeEnum.CAMPAIGN.name())) {
            if (Extensions.isBlankOrNull(dto.getDonationCampaignId())) {
                throw new ServiceException("DonationCampaignId is required");
            }
            DonationCampaign donationCampaign = donationCampaignRepository.findById(dto.getDonationCampaignId())
                    .orElseThrow(() -> new ServiceException("Donation campaign not found"));
            donation.setDonationCampaign(donationCampaign);
            donation.setDonationType(EnumConst.DonationTypeEnum.CAMPAIGN.name());
        }

        // payment account
        if (!dto.getPaymentAccount().isBlankOrNull()) {
            PaymentAccount paymentAccount = paymentAccountRepository.getById(dto.getPaymentAccount());
            donation.setPaymentAccount(paymentAccount);
        }

        // user donate
        if (!Extensions.isBlankOrNull(dto.getUserId())) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ServiceException("User not found"));
            donation.setUser(user);
        }

        donationRepository.save(donation);
        logDataService.create(donation.getId(), EnumConst.LogTypeEnum.DONATION.name(), "create donation " + donation.getId());
        log.info("Donation {} created", donation.getId());

        // forward to payment gateway
        PaymentStrategy paymentGateway = paymentFactory.createStrategy(dto.getPaymentGateway());
        String paymentLink = paymentGateway.createPaymentLink(donation);

        return DonationProfile.builder()
                .paymentLink(paymentLink)
                .donation(donationConverter.toDTO(donation))
                .build();
    }

    @Override
    public DonationDto updateDonation(DonationDto dto) {
        log.info("Update Donation {}", dto.getId());
        Donation donation = donationRepository.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("Donation " + dto.getId() + " not exist"));
        donation.setFirstName(dto.getFirstName());
        donation.setMiddleName(dto.getMiddleName());
        donation.setLastName(dto.getLastName());
        donation.setEmail(dto.getEmail());
        donation.setPhone(dto.getPhone());
        donation.setPaymentDate(dto.getPaymentDate() != null ? DateUtil.formatDateString(dto.getPaymentDate(), DateUtil.TYPE_FORMAT_1) : null);
        donation.setTransactionId(dto.getTransactionId());
        donation.setAmount(dto.getAmount());
        donation.setUpdatedAt(new Date());
        donation.setUpdatedBy(getCurrentUsername());

        updateDonationCampaignAmount(donation, dto.getStatus());
        donation.setStatus(dto.getStatus());
        donationRepository.save(donation);
        logDataService.create(donation.getId(), EnumConst.LogTypeEnum.DONATION.name(), "update donation " + donation.getId());
        return donationConverter.toDTO(donation);
    }

    @Override
    public DonationDto changeStatusDonation(String id, String newStatus) {
        Optional<Donation> optional = donationRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ServiceException("Donation " + id + " not exist");
        }
        Donation donation = optional.get();
        if (donation.getStatus().equals(newStatus)) {
            return donationConverter.toDTO(donation);
        }
        updateDonationCampaignAmount(donation, newStatus);

        log.info("Update status donation {}: {} -> {}", id, donation.getStatus(), newStatus);
        logDataService.create(donation.getId(), EnumConst.LogTypeEnum.DONATION.name(),
                "update status donation " + donation.getId() + ": " + donation.getStatus() + " -> " + newStatus);
        donation.setStatus(newStatus);
        donationRepository.save(donation);

        return donationConverter.toDTO(donation);
    }

    private void updateDonationCampaignAmount(Donation donation, String newStatus) {
        String oldStatus = donation.getStatus();
        if (oldStatus.equals(newStatus)) {
            return;
        }
        if (donation.getDonationCampaign() == null) {
            return;
        }
        DonationCampaign donationCampaign = donation.getDonationCampaign();
        if (donationCampaign.getCurrentAmount() == null) {
            donationCampaign.setCurrentAmount(BigDecimal.ZERO);
        }
        switch (EnumConst.PaymentStatusEnum.valueOf(oldStatus)) {
            case PENDING:
            case FAILED:
            case CANCELED:
                if (newStatus.equals(EnumConst.PaymentStatusEnum.SUCCESS.name())) {
                    donationCampaign.setCurrentAmount(donationCampaign.getCurrentAmount().add(donation.getAmount()));
                }
                break;
            case SUCCESS:
                if (!newStatus.equals(EnumConst.PaymentStatusEnum.SUCCESS.name())) {
                    donationCampaign.setCurrentAmount(donationCampaign.getCurrentAmount().subtract(donation.getAmount()));
                }
                break;
        }
    }
}
