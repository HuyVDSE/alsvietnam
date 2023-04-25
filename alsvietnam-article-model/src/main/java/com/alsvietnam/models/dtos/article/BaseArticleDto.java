package com.alsvietnam.models.dtos.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 9/12/2022
 * Time: 10:25 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseArticleDto {

    @NotBlank(message = "topicId is required")
    private String topicId;

    private String author;

    @NotBlank(message = "label is required")
    private String label;

    private String translator;

}
