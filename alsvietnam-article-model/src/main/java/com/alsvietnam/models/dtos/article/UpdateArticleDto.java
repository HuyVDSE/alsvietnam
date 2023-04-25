package com.alsvietnam.models.dtos.article;

import com.alsvietnam.models.dtos.article.content.UpdateArticleContentDto;
import com.alsvietnam.models.dtos.article.file.UpdateArticleFileRequest;
import com.alsvietnam.models.dtos.article.media.UpdateArticleMediaRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UpdateArticleDto extends BaseArticleDto {

    @NotBlank(message = "Id is required")
    private String id;

    @NotBlank(message = "Status is required")
    private String status;

    @Valid
    private List<UpdateArticleContentDto> articleContents;

    @Valid
    private List<UpdateArticleFileRequest> articleFiles;

    @Valid
    private List<UpdateArticleMediaRequest> articleMedias;
}