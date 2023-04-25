package com.alsvietnam.models.search;

import com.alsvietnam.entities.Article;
import com.alsvietnam.utils.Extensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParameterSearchArticle {

    private String id;

    private String author;

    private String translator;

    private String status;

    private String title;

    private String label;

    private String createdUser;

    private String topicId;

    private Date createFrom;

    private Date createTo;

    private Boolean deleted;

    private Boolean likeArticle;

    // page

    private Long startIndex;

    private Integer pageSize;

    private String sortField;

    private Boolean descSort = false;

    // getter, setter custom
    public void setSortField(String sortField) {
        if (sortField == null) return;
        boolean validField = false;
        if (Extensions.getBaseSortField().contains(sortField)) {
            validField = true;
        } else {
            for (Field field : Article.class.getDeclaredFields()) {
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
