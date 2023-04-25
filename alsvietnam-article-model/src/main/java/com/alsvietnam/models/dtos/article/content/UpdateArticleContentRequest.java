package com.alsvietnam.models.dtos.article.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/12/2022
 * Time: 11:34 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleContentRequest {

    private String articleId;

    @Valid
    @NotEmpty(message = "List contents can't empty")
    private List<UpdateArticleContentDto> articleContents;

}
