package com.alsvietnam.models.dtos.article;

import com.alsvietnam.models.dtos.article.BaseArticleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateBaseArticleDto extends BaseArticleDto {

    @NotBlank(message = "Status is required")
    private String status;
}
