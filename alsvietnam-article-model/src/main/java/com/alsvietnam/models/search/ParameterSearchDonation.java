package com.alsvietnam.models.search;

import com.alsvietnam.entities.Donation;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.Extensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Duc_Huy
 * Date: 8/23/2022
 * Time: 10:41 PM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParameterSearchDonation {

    private String id;

    private String email;

    private String phone;

    private String paymentMethod;

    private String transactionId;

    private String status;

    private String donationType;

    private String donationCampaignId;

    private Date createdFrom;

    private Date createdTo;

    private Date paymentDateFrom;

    private Date paymentDateTo;

    // build

    private Boolean buildForDashboard;

    // page

    private Long startIndex;

    private Integer pageSize;

    private String sortField;

    private Boolean descSort = false;

    // getter, setter custom

    public void setCreatedFrom(String createdFrom, String dateType) {
        if (!Extensions.isBlankOrNull(createdFrom)) {
            Date date = DateUtil.formatDateString(createdFrom, dateType);
            this.createdFrom = DateUtil.customFromDate(date);
        }
    }

    public void setCreatedTo(String createdTo, String dateType) {
        if (!Extensions.isBlankOrNull(createdTo)) {
            Date date = DateUtil.formatDateString(createdTo, dateType);
            this.createdTo = DateUtil.customToDate(date);
        }
    }

    public void setPaymentDateFrom(String paymentDateFrom, String dateType) {
        if (!Extensions.isBlankOrNull(paymentDateFrom)) {
            Date date = DateUtil.formatDateString(paymentDateFrom, dateType);
            this.paymentDateFrom = DateUtil.customFromDate(date);
        }
    }

    public void setPaymentDateTo(String paymentDateTo, String dateType) {
        if (!Extensions.isBlankOrNull(paymentDateTo)) {
            Date date = DateUtil.formatDateString(paymentDateTo, dateType);
            this.paymentDateTo = DateUtil.customToDate(date);
        }
    }

    public void setSortField(String sortField) {
        if (sortField == null) return;
        boolean validField = false;
        if (Extensions.getBaseSortField().contains(sortField)) {
            validField = true;
        } else {
            for (Field field : Donation.class.getDeclaredFields()) {
                if (field.getName().equals(sortField)) {
                    validField = true;
                    break;
                }
            }
        }
        if (!validField) {
            throw new IllegalArgumentException("Invalid sort field");
        }
        this.sortField = sortField;
    }
}
