package com.alsvietnam.models.dtos.article.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateArticleFileDto extends CreateArticleFileDto {

    private String id;

    private String articleId;
}
