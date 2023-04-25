package com.alsvietnam.models.search;

import com.alsvietnam.entities.DonationCampaign;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.Extensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 12:52 AM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParameterSearchCampaign {

    private String id;

    private String title;

    private Date dateStartFrom;

    private Date dateStartTo;

    private Date dateEndFrom;

    private Date dateEndTo;

    private BigDecimal expectedAmountFrom;

    private BigDecimal expectedAmountTo;

    private BigDecimal currentAmountFrom;

    private BigDecimal currentAmountTo;

    private String status;

    private Boolean active;

    private boolean deleted;

    // page

    private Long startIndex;

    private Integer pageSize;

    private String sortField;

    private Boolean descSort = false;

    public void setDateStartFrom(String dateStartFrom, String dateType) {
        if (!Extensions.isBlankOrNull(dateStartFrom)) {
            Date date = DateUtil.formatDateString(dateStartFrom, dateType);
            this.dateStartFrom = DateUtil.customFromDate(date);
        }
    }

    public void setDateStartTo(String dateStartTo, String dateType) {
        if (!Extensions.isBlankOrNull(dateStartTo)) {
            Date date = DateUtil.formatDateString(dateStartTo, dateType);
            this.dateStartTo = DateUtil.customToDate(date);
        }
    }

    public void setDateEndFrom(String dateEndFrom, String dateType) {
        if (!Extensions.isBlankOrNull(dateEndFrom)) {
            Date date = DateUtil.formatDateString(dateEndFrom, dateType);
            this.dateEndFrom = DateUtil.customFromDate(date);
        }
    }

    public void setDateEndTo(String dateEndTo, String dateType) {
        if (!Extensions.isBlankOrNull(dateEndTo)) {
            Date date = DateUtil.formatDateString(dateEndTo, dateType);
            this.dateEndTo = DateUtil.customToDate(date);
        }
    }

    public void setExpectedAmountFrom(String expectedAmountFrom) {
        if (!Extensions.isBlankOrNull(expectedAmountFrom)) {
            this.expectedAmountFrom = new BigDecimal(expectedAmountFrom);
        }
    }

    public void setExpectedAmountTo(String expectedAmountTo) {
        if (!Extensions.isBlankOrNull(expectedAmountTo)) {
            this.expectedAmountTo = new BigDecimal(expectedAmountTo);
        }
    }

    public void setCurrentAmountFrom(String currentAmountFrom) {
        if (!Extensions.isBlankOrNull(currentAmountFrom)) {
            this.currentAmountFrom = new BigDecimal(currentAmountFrom);
        }
    }

    public void setCurrentAmountTo(String currentAmountTo) {
        if (!Extensions.isBlankOrNull(currentAmountTo)) {
            this.currentAmountTo = new BigDecimal(currentAmountTo);
        }
    }

    public void setSortField(String sortField) {
        if (sortField == null) return;
        boolean validField = false;
        if (Extensions.getBaseSortField().contains(sortField)) {
            validField = true;
        } else {
            for (Field field : DonationCampaign.class.getDeclaredFields()) {
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
