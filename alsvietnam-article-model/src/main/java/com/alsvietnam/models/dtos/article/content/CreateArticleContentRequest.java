package com.alsvietnam.models.dtos.article.content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateArticleContentRequest {

    private String articleId;

    @Valid
    @NotEmpty(message = "List contents can't empty")
    private List<CreateArticleContentDto> articleContents;
}
