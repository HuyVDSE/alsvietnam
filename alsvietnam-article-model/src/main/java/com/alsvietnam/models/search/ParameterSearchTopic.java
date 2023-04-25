package com.alsvietnam.models.search;

import com.alsvietnam.entities.Topic;
import com.alsvietnam.utils.Extensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterSearchTopic {

    private String id;

    private String titleVietnamese;

    private String titleEnglish;

    private String type;

    private String topicParentId;

    private List<String> topicParentIds;

    private Boolean deleted;

    private Boolean active;

    private int articleBuildLimit = 5;

    // page

    private Long startIndex = 0L;

    private Integer pageSize;

    private String sortField;

    private Boolean descSort = false;

    // build

    private boolean buildArticles = false;

    private boolean buildTopicTree = false;

    // getter, setter custom

    public void setSortField(String sortField) {
        if (sortField == null) return;
        boolean validField = false;
        if (Extensions.getBaseSortField().contains(sortField)) {
            validField = true;
        } else {
            for (Field field : Topic.class.getDeclaredFields()) {
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

    public void setArticleBuildLimit(Integer articleBuildLimit) {
        if (articleBuildLimit == null) return;
        this.articleBuildLimit = articleBuildLimit;
    }
}
