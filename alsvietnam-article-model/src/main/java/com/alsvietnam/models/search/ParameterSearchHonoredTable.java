package com.alsvietnam.models.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:33 PM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParameterSearchHonoredTable {

    private String id;

    private List<String> ids;

    private String title;

    private Long quarter;

    private Long year;

    private Boolean active;

    private Boolean deleted;

    // page

    private Long startIndex;

    private Integer pageSize;

    private String sortField;

    private Boolean descSort = false;

    // build

    private boolean buildUser = false;

    // getter, setter custom


    public void setBuildUser(Boolean buildUser) {
        if (buildUser == null) return;
        this.buildUser = buildUser;
    }
}
