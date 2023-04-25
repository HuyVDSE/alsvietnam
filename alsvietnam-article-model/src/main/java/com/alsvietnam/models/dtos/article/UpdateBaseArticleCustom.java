package com.alsvietnam.models.dtos.article;

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
public class UpdateBaseArticleCustom  {

    private String id;

    private UpdateBaseArticleDto articleDto;
}
