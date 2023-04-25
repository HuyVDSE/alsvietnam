package com.alsvietnam.repository;

import com.alsvietnam.entities.Comment;
import com.alsvietnam.models.search.ParameterSearchComment;
import com.alsvietnam.models.wrapper.ListWrapper;

public interface CommentRepositoryCustom  {

    ListWrapper<Comment> searchComment(ParameterSearchComment parameterSearchComment);
}
