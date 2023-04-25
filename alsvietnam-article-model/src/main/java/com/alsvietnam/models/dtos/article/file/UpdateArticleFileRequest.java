package com.alsvietnam.models.dtos.article.file;

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
public class UpdateArticleFileRequest {

    @NotBlank(message = "Id article file is required")
    private String id;

    @NotBlank(message = "Article Id is required")
    private String articleId;

    @NotNull(message = "File is required")
    private MultipartFile articleFile;

    @NotNull(message = "Language is required")
    private String language;
}
