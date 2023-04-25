package com.alsvietnam.repository.impl;

import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Set;

@ExtensionMethod(Extensions.class)
public class BaseRepositoryCustom {

    public final String ENTITY_GRAPH = "javax.persistence.loadgraph";

    @PersistenceContext
    protected EntityManager em;

    /**
     *  This method is used to get the total count of the query with multiple join conditions
     * */
    public <T> Long getTotalCount(CriteriaBuilder builder, CriteriaQuery<?> selectQuery, Root<T> root) {
        CriteriaQuery<Long> query = createCountQuery(builder, selectQuery, root);
        return this.em.createQuery(query).getSingleResult();
    }

    private <T> CriteriaQuery<Long> createCountQuery(CriteriaBuilder builder, CriteriaQuery<?> criteria, Root<T> root) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(root.getModel());

        doJoins(root.getJoins(), countRoot);
        doJoinsOnFetches(root.getFetches(), countRoot);

        countQuery.select(builder.count(countRoot));
        countQuery.where(criteria.getRestriction());

        countRoot.alias(root.getAlias());

        return countQuery.distinct(criteria.isDistinct());
    }

    @SuppressWarnings("unchecked")
    private void doJoinsOnFetches(Set<? extends Fetch<?, ?>> joins, Root<?> root) {
        doJoins((Set<? extends Join<?, ?>>) joins, root);
    }

    private void doJoins(Set<? extends Join<?, ?>> joins, Root<?> root) {
        for (Join<?, ?> join : joins) {
            Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
            joined.alias(join.getAlias());
            doJoins(join.getJoins(), joined);
        }
    }

    private void doJoins(Set<? extends Join<?, ?>> joins, Join<?, ?> root) {
        for (Join<?, ?> join : joins) {
            Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
            joined.alias(join.getAlias());
            doJoins(join.getJoins(), joined);
        }
    }

    protected void setSortField(CriteriaBuilder builder, CriteriaQuery<?> query, Root<?> root, String sortField, boolean isDescSort) {
        if (sortField.isBlankOrNull()) {
            query.orderBy(builder.desc(root.get("createdAt")));
        } else {
            if (isDescSort) {
                query.orderBy(builder.desc(root.get(sortField)));
            } else {
                query.orderBy(builder.asc(root.get(sortField)));
            }
        }
    }
}
