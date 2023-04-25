package com.alsvietnam.models.profiles;

import com.alsvietnam.models.dtos.article.ArticleCustom;
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
public class TopicProfile {

    private String id;

    private String titleEnglish;

    private String topicParentId;

    private String description;

    private String titleVietnamese;

    private String createdBy;

    private Date createdAt;

    private String updatedBy;

    private Date updatedAt;

    private Boolean active;

    private List<TopicProfile> topicChild;

    private List<ArticleCustom> articles;
}
