package com.alsvietnam.models.dtos.article.content;

import com.alsvietnam.annotations.validator.ValueOfEnum;
import com.alsvietnam.utils.EnumConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 9/12/2022
 * Time: 10:08 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateArticleContentDto {

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    @NotBlank(message = "content is required")
    private String content;

    @NotBlank(message = "language is required")
    @ValueOfEnum(enumClass = EnumConst.LanguageTypeEnum.class, message = "Invalid language type")
    private String language;

}
