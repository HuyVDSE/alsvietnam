package com.alsvietnam.converter;

import com.alsvietnam.entities.Article;
import com.alsvietnam.entities.Comment;
import com.alsvietnam.entities.User;
import com.alsvietnam.models.dtos.comment.CreateCommentDto;
import com.alsvietnam.models.dtos.comment.UpdateCommentDto;
import com.alsvietnam.models.profiles.CommentProfile;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@ExtensionMethod(Extensions.class)
public class CommentConverter extends BaseConverter {

    private final UserConverter userConverter;

    public Comment fromCreateDto(CreateCommentDto dto, Article article, User user) {
        return Comment.builder()
                .content(dto.getContent())
                .likeNumber(0)
                .article(article)
                .user(user)
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .build();
    }

    public Comment fromUpdateDto(UpdateCommentDto dto, Comment comment) {
        comment.setContent(dto.getContent());
        comment.setUpdatedAt(new Date());
        comment.setUpdatedBy(userService.getUsernameLogin());
        return comment;
    }


    public CommentProfile toProfile(Comment comment, User user, String userId) {
        return CommentProfile.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .flag(comment.getFlag())
                .likeNumber(comment.getLikeNumber())
                .articleId(comment.getArticle().getId())
                .userId(userId)
                .userProfile(userConverter.toProfile(user))
                .createdAt(comment.getCreatedAt())
                .createdBy(comment.getCreatedBy())
                .updatedAt(comment.getUpdatedAt())
                .updatedBy(comment.getUpdatedBy())
                .build();
    }

    public CommentProfile toProfile(Comment comment, User user, Set<Comment> comments, String userId) {
        if (comments.contains(comment))
            return CommentProfile.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .flag(comment.getFlag())
                    .likeNumber(comment.getLikeNumber())
                    .articleId(comment.getArticle().getId())
                    .userId(userId)
                    .userProfile(userConverter.toProfile(user))
                    .createdAt(comment.getCreatedAt())
                    .createdBy(comment.getCreatedBy())
                    .updatedAt(comment.getUpdatedAt())
                    .updatedBy(comment.getUpdatedBy())
                    .build();

        return CommentProfile.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .flag(comment.getFlag())
                .likeNumber(comment.getLikeNumber())
                .articleId(comment.getArticle().getId())
                .userId(null)
                .userProfile(userConverter.toProfile(user))
                .createdAt(comment.getCreatedAt())
                .createdBy(comment.getCreatedBy())
                .updatedAt(comment.getUpdatedAt())
                .updatedBy(comment.getUpdatedBy())
                .build();
    }

    public List<CommentProfile> toProfiles(List<Comment> comments) {
        Set<String> userIds = comments.stream().map(comment -> comment.getUser().getId()).collect(Collectors.toSet());
        List<User> users = userRepository.findAllById(userIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        if (!userService.getUsernameLogin().equals("anonymousUser")) {
            User user = userService.getUserLogin();
            Set<Comment> commentList = commentRepository.findByUsers_Id(user.getId());
            if (Extensions.isNullOrEmpty(commentList))
                return comments.stream().map(comment -> toProfile(comment, userMap.get(comment.getUser().getId()),
                                null))
                        .collect(Collectors.toList());
            return comments.stream().map(comment -> toProfile(comment, userMap.get(comment.getUser().getId()),
                            commentList, user.getId()))
                    .collect(Collectors.toList());
        }

        return comments.stream().map(comment -> toProfile(comment, userMap.get(comment.getUser().getId()), null))
                .collect(Collectors.toList());
    }
}
