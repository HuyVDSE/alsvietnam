package com.alsvietnam.models.dtos.article.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ArticleFileDto extends UpdateArticleFileDto {

    private String createdBy;

    private Date createdAt;

    private String updatedBy;

    private Date updatedAt;
}
