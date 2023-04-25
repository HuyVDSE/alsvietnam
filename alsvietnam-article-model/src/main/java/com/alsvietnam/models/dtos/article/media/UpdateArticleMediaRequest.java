package com.alsvietnam.models.dtos.article.media;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UpdateArticleMediaRequest {

    @NotBlank(message = "Id article file is required")
    private String id;

    @NotBlank(message = "Article Id is required")
    private String articleId;

    @NotNull(message = "File is required")
    private MultipartFile articleMedia;

    private Boolean coverImage;

    private Integer index;
}
