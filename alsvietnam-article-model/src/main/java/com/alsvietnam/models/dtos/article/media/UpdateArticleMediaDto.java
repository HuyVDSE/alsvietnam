package com.alsvietnam.models.dtos.article.media;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UpdateArticleMediaDto extends CreateArticleMediaDto {

    private String id;

    private String articleId;
}
