package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.Notification;
import com.alsvietnam.models.search.ParameterSearchNotification;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.NotificationRepositoryCustom;
import com.alsvietnam.utils.Extensions;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepositoryCustomImpl extends BaseRepositoryCustom implements NotificationRepositoryCustom {

    @Override
    public ListWrapper<Notification> searchNotification(ParameterSearchNotification searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Notification> query = builder.createQuery(Notification.class);
        Root<Notification> root = query.from(Notification.class);

        List<Predicate> predicates = new ArrayList<>();
        if (!Extensions.isBlankOrNull(searchParam.getId())) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getTitle())) {
            predicates.add(builder.like(builder.lower(root.get("title")),
                    "%" + searchParam.getTitle().toLowerCase() + "%"));
        }
        if (!Extensions.isBlankOrNull(searchParam.getContent())) {
            predicates.add(builder.like(builder.lower(root.get("content")),
                    "%" + searchParam.getContent().toLowerCase() + "%"));
        }
        if (searchParam.getStatus() != null) {
            predicates.add(builder.equal(root.get("status"), searchParam.getStatus()));
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

        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());

        // Phân trang
        TypedQuery<Notification> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(searchParam.getPageSize());
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        List<Notification> results = typedQuery.getResultList();

        // tổng số bản ghi
        Long total = getTotalCount(builder, query, root);
        return ListWrapper.<Notification>builder()
                .total(total)
                .totalPage((total - 1) / searchParam.getPageSize() + 1)
                .maxResult(searchParam.getPageSize())
                .currentPage(searchParam.getStartIndex() / searchParam.getPageSize() + 1)
                .data(results)
                .build();
    }
}
