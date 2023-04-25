package com.alsvietnam.models.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParameterSearchTopOrganizationSupport {

    private String id;

    private String organizationName;

    private Boolean active;

    private Boolean deleted;

    private Date createFrom;

    private Date createTo;

    // page

    private Long startIndex;

    private Integer pageSize;

    private String sortField;

    private Boolean descSort = false;
}
