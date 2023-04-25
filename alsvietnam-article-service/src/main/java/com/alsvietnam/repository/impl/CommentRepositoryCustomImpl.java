package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.Comment;
import com.alsvietnam.models.search.ParameterSearchComment;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.CommentRepositoryCustom;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@ExtensionMethod(Extensions.class)
public class CommentRepositoryCustomImpl extends BaseRepositoryCustom implements CommentRepositoryCustom {
    @Override
    public ListWrapper<Comment> searchComment(ParameterSearchComment searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Comment> query = builder.createQuery(Comment.class);
        Root<Comment> root = query.from(Comment.class);

        List<Predicate> predicates = new ArrayList<>();
        if (!Extensions.isBlankOrNull(searchParam.getId())) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getContent())) {
            predicates.add(builder.like(builder.lower(root.get("content")),
                    "%" + searchParam.getContent().toLowerCase() + "%"));
        }
        //True mean get comment reported
        if (Boolean.TRUE.equals(searchParam.getFlag())) {
            predicates.add(builder.isNotNull(root.get("flag")));
        } else if (Boolean.FALSE.equals(searchParam.getFlag())) {
            predicates.add(builder.isNull(root.get("flag")));
        }
        if (!Extensions.isBlankOrNull(searchParam.getArticleId())) {
            predicates.add(builder.equal(root.get("article").get("id"), searchParam.getArticleId()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getUserId())) {
            predicates.add(builder.equal(root.get("user").get("id"), searchParam.getUserId()));
        }
        if (searchParam.getCreateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), searchParam.getCreateTo()));
        }
        if (searchParam.getCreateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), searchParam.getCreateFrom()));
        }
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());

        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        // Phân trang
        TypedQuery<Comment> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(searchParam.getPageSize());
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        List<Comment> results = typedQuery.getResultList();

        // tổng số bản ghi
        Long total = getTotalCount(builder, query, root);
        return ListWrapper.<Comment>builder()
                .total(total)
                .totalPage((total - 1) / searchParam.getPageSize() + 1)
                .maxResult(searchParam.getPageSize())
                .currentPage(searchParam.getStartIndex() / searchParam.getPageSize() + 1)
                .data(results)
                .build();
    }
}
