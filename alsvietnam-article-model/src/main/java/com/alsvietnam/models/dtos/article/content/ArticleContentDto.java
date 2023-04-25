package com.alsvietnam.models.dtos.article.content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * Duc_Huy
 * Date: 9/12/2022
 * Time: 10:49 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ArticleContentDto extends UpdateArticleContentDto {

    private String createdBy;

    private Date createdAt;

    private String updatedBy;

    private Date updatedAt;

}
