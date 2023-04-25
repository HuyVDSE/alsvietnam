package com.alsvietnam.models.dtos.payment;

import com.alsvietnam.entities.Donation;
import com.alsvietnam.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Duc_Huy
 * Date: 11/13/2022
 * Time: 10:54 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class DonationCustomDTO {

    // for donation campaign type
    private String id;
    private String lastName;
    private String firstName;
    private BigDecimal amount;
    private String paymentDate;
    private String createdAt;
    private boolean hiddenInfo = false;

    // for donation general type

    private BigDecimal totalGeneralFund;

    private BigDecimal totalWebsiteFund;

    private BigDecimal totalDocumentFund;

    private BigDecimal totalStoryFund;

    public DonationCustomDTO(Donation donation) {
        this.id = donation.getId();
        this.lastName = donation.getLastName();
        this.firstName = donation.getFirstName();
        this.amount = donation.getAmount();
        this.hiddenInfo = donation.getHiddenInfo();
        setPaymentDate(donation.getPaymentDate());
        setCreatedAt(donation.getCreatedAt());
    }

    public DonationCustomDTO(BigDecimal totalGeneralFund, BigDecimal totalWebsiteFund,
                             BigDecimal totalDocumentFund, BigDecimal totalStoryFund) {
        this.totalGeneralFund = totalGeneralFund;
        this.totalWebsiteFund = totalWebsiteFund;
        this.totalDocumentFund = totalDocumentFund;
        this.totalStoryFund = totalStoryFund;
    }

    // getter, setter custom

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = DateUtil.convertDateToString(paymentDate, DateUtil.TYPE_FORMAT_1);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = DateUtil.convertDateToString(createdAt, DateUtil.TYPE_FORMAT_1);
    }
}
