package com.alsvietnam.service.impl;

import com.alsvietnam.entities.Article;
import com.alsvietnam.entities.Comment;
import com.alsvietnam.entities.User;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.comment.CreateCommentDto;
import com.alsvietnam.models.dtos.comment.ReportCommentDto;
import com.alsvietnam.models.dtos.comment.UpdateCommentDto;
import com.alsvietnam.models.profiles.CommentProfile;
import com.alsvietnam.models.search.ParameterSearchComment;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.CommentService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@ExtensionMethod(value = Extensions.class)
@Slf4j
public class CommentServiceImpl extends BaseService implements CommentService {

    @Override
    public CommentProfile createComment(CreateCommentDto dto) {
        Article article = articleRepository.findById(dto.getArticleId())
                .orElseThrow(() -> new ServiceException("Article with id " + dto.getArticleId() + " not found"));
        User user = userService.getUserLogin();

        Comment comment = commentConverter.fromCreateDto(dto, article, user);
        commentRepository.save(comment);

        return commentConverter.toProfile(comment, user, null);
    }

    @Override
    public CommentProfile updateComment(UpdateCommentDto dto) {
        Comment comment = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("Comment with id " + dto.getId() + " not found"));
        User user = userService.getUserLogin();

        if (!comment.getArticle().getId().equals(dto.getArticleId()))
            throw new ServiceException("Article id: " + dto.getArticleId() + " not match");
        if (!comment.getUser().getId().equals(user.getId()))
            throw new ServiceException("Can't update comment of other user");

        comment = commentConverter.fromUpdateDto(dto, comment);
        commentRepository.save(comment);

        if (comment.getUsers().contains(user))
            return commentConverter.toProfile(comment, comment.getUser(), user.getId());

        return commentConverter.toProfile(comment, comment.getUser(), null);
    }

    @Override
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("Comment with id " + commentId + " not found"));
        User user = userService.getUserLogin();
        if (user.getRole().getName().equals(EnumConst.RoleEnum.ROLE_ADMIN.name())) {
            commentRepository.delete(comment);
            return;
        }
        if (!comment.getUser().getId().equals(user.getId()))
            throw new ServiceException("Can't delete comment of other user");

        commentRepository.delete(comment);
    }

    @Override
    public CommentProfile reportComment(ReportCommentDto dto) {
        Comment comment = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("Comment with id " + dto.getId() + " not found"));

        comment.setFlag(dto.getFlagContent());
        commentRepository.save(comment);

        User user = userService.getUserLogin();
        if (comment.getUsers().contains(user))
            return commentConverter.toProfile(comment, comment.getUser(), user.getId());

        return commentConverter.toProfile(comment, comment.getUser(), null);
    }

    @Override
    public ListWrapper<CommentProfile> searchComment(ParameterSearchComment parameterSearchComment) {
        ListWrapper<Comment> wrapper = commentRepository.searchComment(parameterSearchComment);

        List<CommentProfile> commentProfiles = commentConverter.toProfiles(wrapper.getData());
        return ListWrapper.<CommentProfile>builder()
                .total(wrapper.getTotal())
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .totalPage(wrapper.getTotalPage())
                .data(commentProfiles)
                .build();
    }

    @Override
    public CommentProfile likeComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("Comment with id " + commentId + " not found"));
        User user = userService.getUserLogin();

        Integer likeNumber = comment.getLikeNumber();
        comment.setLikeNumber(likeNumber + 1);
        Set<User> users = comment.getUsers();
        if (user == null) users = new HashSet<>();
        if (users.contains(user)) throw  new ServiceException("User with id " + user.getId() + " already like");
        users.add(user);

        commentRepository.save(comment);

        return commentConverter.toProfile(comment, comment.getUser(), user.getId());
    }

    @Override
    public CommentProfile unlikeComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("Comment with id " + commentId + " not found"));
        User user = userService.getUserLogin();

        Integer likeNumber = comment.getLikeNumber();
        Set<User> likeUsers = comment.getUsers();
        if (likeNumber == 0 || likeUsers == null || !likeUsers.contains(user)) {
            throw new ServiceException("User with id " + user.getId() + " not like comment yet");
        }

        comment.setLikeNumber(likeNumber - 1);
        likeUsers.remove(user);

        commentRepository.save(comment);

        return commentConverter.toProfile(comment, comment.getUser(), null);
    }
}
