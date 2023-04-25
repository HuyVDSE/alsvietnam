package com.alsvietnam.models.dtos.article.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateArticleFileRequest {

    @NotNull(message = "file is required")
    private MultipartFile articleFile;

    @NotBlank(message = "language is required")
    private String language;
}
