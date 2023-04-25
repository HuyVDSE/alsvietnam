package com.alsvietnam.models.dtos.article;

import lombok.Builder;
import lombok.Data;

/**
 * Duc_Huy
 * Date: 12/1/2022
 * Time: 7:41 PM
 */

@Data
@Builder
public class ArticleCustom {

    private String id;

    private String topicId;

    private String englishTitle;

    private String vietnameseTitle;

}
