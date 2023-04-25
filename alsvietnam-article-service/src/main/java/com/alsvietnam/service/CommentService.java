package com.alsvietnam.service;

import com.alsvietnam.models.dtos.comment.CreateCommentDto;
import com.alsvietnam.models.dtos.comment.ReportCommentDto;
import com.alsvietnam.models.dtos.comment.UpdateCommentDto;
import com.alsvietnam.models.profiles.CommentProfile;
import com.alsvietnam.models.search.ParameterSearchComment;
import com.alsvietnam.models.wrapper.ListWrapper;

public interface CommentService {
    CommentProfile createComment(CreateCommentDto dto);

    CommentProfile updateComment(UpdateCommentDto dto);

    void deleteComment(String commentId);

    CommentProfile reportComment(ReportCommentDto dto);

    ListWrapper<CommentProfile> searchComment(ParameterSearchComment parameterSearchComment);

    CommentProfile likeComment(String commentId);

    CommentProfile unlikeComment(String commentId);
}
