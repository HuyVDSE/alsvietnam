package com.alsvietnam.models.profiles;

import com.alsvietnam.models.dtos.article.content.ArticleContentDto;
import com.alsvietnam.models.dtos.article.file.ArticleFileDto;
import com.alsvietnam.models.dtos.article.media.ArticleMediaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleProfile {

    private String id;

    private String author;

    private String translator;

    private String label;

    private String status;

    private String topicId;

    private String createdUserId;

    private ArticleMediaDto coverImage;

    private Integer likeNumber;

    private String userId;

    private List<ArticleContentDto> articleContents;

    private List<ArticleFileDto> articleFiles;

    private List<ArticleMediaDto> articleMedias;

    private String createdBy;

    private Date createdAt;

    private String updatedBy;

    private Date updatedAt;

}
