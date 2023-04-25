package com.alsvietnam.models.dtos.article.content;

import com.alsvietnam.models.dtos.article.content.CreateArticleContentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 9/12/2022
 * Time: 10:33 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateArticleContentDto extends CreateArticleContentDto {

    @NotBlank(message = "Id article content is required")
    private String id;

    @NotBlank(message = "Article Id is required")
    private String articleId;

}