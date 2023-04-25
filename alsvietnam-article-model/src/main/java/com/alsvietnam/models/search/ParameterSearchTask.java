package com.alsvietnam.models.search;

import com.alsvietnam.entities.Task;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.Extensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/16/2022
 * Time: 10:35 AM
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterSearchTask {

    private String id;

    private String status;

    private List<String> statuses;

    private String teamId;

    private String articleId;

    private String managerId;

    private String userId;

    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})?$", message = "from_start_date invalid")
    private String fromStartDate;

    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})?$", message = "to_start_date invalid")
    private String toStartDate;

    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})?$", message = "from_end_date invalid")
    private String fromEndDate;

    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})?$", message = "to_end_date invalid")
    private String toEndDate;

    private Date doneTaskFrom;

    private Date doneTaskTo;

    private Date createdFrom;

    private Date createdTo;

    // page

    private Long startIndex = 0L;

    private Integer pageSize;

    private String sortField;

    private Boolean descSort = false;

    // build

    private boolean buildArticle = false;

    private boolean buildTeam = true;

    private boolean buildManager = true;

    private boolean buildUsersDoTask = true;

    // getter, setter custom

    public void setFromEndDate(String fromEndDate) {
        this.fromEndDate = fromEndDate;
    }

    public void setFromEndDate(Date fromEndDate) {
        this.fromEndDate = DateUtil.convertDateToString(fromEndDate, DateUtil.TYPE_FORMAT_2);
    }

    public void setToEndDate(String toEndDate) {
        this.toEndDate = toEndDate;
    }

    public void setToEndDate(Date toEndDate) {
        this.toEndDate = DateUtil.convertDateToString(toEndDate, DateUtil.TYPE_FORMAT_2);
    }

    public void setDoneTaskFrom(String doneTaskFrom, String dateType) {
        if (!Extensions.isBlankOrNull(doneTaskFrom)) {
            this.doneTaskFrom = DateUtil.formatDateString(doneTaskFrom, dateType);
        }
    }

    public void setDoneTaskTo(String doneTaskTo, String dateType) {
        if (!Extensions.isBlankOrNull(doneTaskTo)) {
            this.doneTaskTo = DateUtil.formatDateString(doneTaskTo, dateType);
        }
    }

    public void setCreatedFrom(String createdFrom, String dateType) {
        if (!Extensions.isBlankOrNull(createdFrom)) {
            this.createdFrom = DateUtil.formatDateString(createdFrom, dateType);
        }
    }

    public void setCreatedTo(String createdTo, String dateType) {
        if (!Extensions.isBlankOrNull(createdTo)) {
            this.createdTo = DateUtil.formatDateString(createdTo, dateType);
        }
    }

    public void setSortField(String sortField) {
        if (sortField == null) return;
        boolean validField = false;
        if (Extensions.getBaseSortField().contains(sortField)) {
            validField = true;
        } else {
            for (Field field : Task.class.getDeclaredFields()) {
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
