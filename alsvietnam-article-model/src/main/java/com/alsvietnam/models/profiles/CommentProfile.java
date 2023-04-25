package com.alsvietnam.models.profiles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentProfile {

    private String id;

    private String flag;

    private String content;

    private Integer likeNumber;

    private String articleId;

    private String userId;

    private UserProfile userProfile;

    private String createdBy;

    private Date createdAt;

    private String updatedBy;

    private Date updatedAt;
}
