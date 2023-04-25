package com.alsvietnam.models.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Duc_Huy
 * Date: 10/23/2022
 * Time: 10:12 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParameterSearchStory {

    private String id;

    private String title;

    private String articleId;

    private String userId;

    private Boolean deleted;

    // page

    private Long startIndex;

    private Integer pageSize;

    private String sortField;

    private Boolean descSort = false;

}
