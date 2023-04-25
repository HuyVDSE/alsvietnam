package com.alsvietnam.models.search;

import com.alsvietnam.entities.User;
import com.alsvietnam.utils.Extensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/6/2022
 * Time: 11:06 PM
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterSearchUser {

    private String id;

    private List<String> ids;

    private String username;

    private String email;

    private String phone;

    private String teamName;

    private List<String> teamIds;

    private List<String> roles;

    private Boolean deleted;

    private Boolean status;

    private Boolean approveStatus;

    private Date createdFrom;

    private Date createdTo;

    // page

    private Long startIndex = 0L;

    private Integer pageSize;

    private String sortField;

    private Boolean descSort = false;

    // build

    private boolean buildRole = false;

    private boolean buildTeam = false;

    // getter, setter custom

    public void setSortField(String sortField) {
        if (sortField == null) return;
        boolean validField = false;
        if (Extensions.getBaseSortField().contains(sortField)) {
            validField = true;
        } else {
            for (Field field : User.class.getDeclaredFields()) {
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
