package com.alsvietnam.models.dtos.article.media;

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
public class CreateArticleMediaDto {

    private String name;

    private String url;

    private String extension;

    private String mime;

    private String type;

    private Boolean coverImage;

    private Integer index;
}
